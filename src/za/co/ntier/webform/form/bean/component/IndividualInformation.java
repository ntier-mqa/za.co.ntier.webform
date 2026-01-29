package za.co.ntier.webform.form.bean.component;

import java.util.ArrayList;
import java.util.List;

import org.adempiere.exceptions.AdempiereException;
import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MRefList;
import org.compiere.model.MUser;
import org.compiere.model.Query;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.ValueNamePair;

import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.api.model.X_ZZ_EDP_Application;
import za.co.ntier.api.model.X_ZZ_LI_HighestEducation;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.viewmodel.DiscretionaryGrantsApplicationProgramVM;

public class IndividualInformation implements ISaveForm
{

	private static final Integer							NQF_AD_REFERENCE_ID		= 1000023;
	private final MenuContextInfo							menuContextInfo;
	private final DiscretionaryGrantsApplicationProgramVM	parentVM;

	// --- Fields for databinding (ZUL) ---
	private String											firstName;
	private String											surname;
	private String											idNumber;

	private Integer											age;
	private String											gender;

	// Applicant Details
	private String											contactNumber;
	private String											alternateContactNumber;
	private String											email;
	private String											currentPosition;
	private Integer											highestEducationId;
	private String											nqfLevel;
	private String											executiveStatus;

	// dropdown models
	private List<ValueNamePair>								nqfLevelOptions			= new ArrayList<>();
	private ValueNamePair									selectedNqfLevel;

	private List<KeyNamePair>								highestEducationOptions	= new ArrayList<>();
	private KeyNamePair										selectedHighestEducation;

	public IndividualInformation(MenuContextInfo menuContextInfo, DiscretionaryGrantsApplicationProgramVM parentVM)
	{
		this.menuContextInfo = menuContextInfo;
		this.parentVM = parentVM;
	}

	public void initComponent(X_ZZ_Application_Form applicationForm)
	{

		loadNqfLevelOptionsFromReference();
		loadHighestEducationOptionsFromTable();
		autofillFromLoggedInUserIfEmpty();

		if (applicationForm == null || applicationForm.get_ID() <= 0)
		{
			return;
		}

		X_ZZ_EDP_Application edp = new Query(Env.getCtx(), X_ZZ_EDP_Application.Table_Name,
				X_ZZ_EDP_Application.COLUMNNAME_ZZ_Application_Form_ID + "=?", null)
						.setParameters(applicationForm.get_ID()).setOnlyActiveRecords(true).first();

		this.firstName = edp.getName();
		this.surname = edp.getSurname();
		this.idNumber = edp.getZZ_ID_Passport_No();
		this.contactNumber = edp.getCellphonenumber();
		this.alternateContactNumber = edp.getAltCellphonenumber();
		this.email = edp.getEMail();
		this.currentPosition = edp.getPosition();
		this.highestEducationId = edp.getZZ_LI_HighestEducation_ID();
		this.nqfLevel = edp.getZZ_NQF_Level();
		this.age = edp.getAge();
		this.gender = edp.getZZGender();

		if (edp.isExecutive())
			this.executiveStatus = "EXECUTIVE";
		else if (edp.isAspiringExecutive())
			this.executiveStatus = "ASPIRING";
		else
			this.executiveStatus = null;

		// Highest education selection (value is id-as-string)
		selectedHighestEducation = highestEducationOptions.stream()
				.filter(x -> x.getID().equals(String.valueOf(this.highestEducationId))).findFirst().orElse(null);

		// NQF selection
		selectedNqfLevel = nqfLevelOptions.stream().filter(x -> x.getValue().equals(this.nqfLevel)).findFirst()
				.orElse(null);

	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm)
	{
		if (applicationForm == null || applicationForm.get_ID() <= 0)
			return;

		X_ZZ_EDP_Application edp = new Query(Env.getCtx(), X_ZZ_EDP_Application.Table_Name,
				X_ZZ_EDP_Application.COLUMNNAME_ZZ_Application_Form_ID + "=?", trxName)
						.setParameters(applicationForm.get_ID()).setOnlyActiveRecords(true).first();

		// --- Create if missing ---
		if (edp == null)
		{
			edp = new X_ZZ_EDP_Application(Env.getCtx(), 0, trxName);

			edp.setZZ_Application_Form_ID(applicationForm.get_ID());

			edp.setAD_Org_ID(applicationForm.getAD_Org_ID());
			edp.saveEx(trxName);

		}
		else
		{
			edp.set_TrxName(trxName);
		}

		edp.setName(firstName);
		edp.setSurname(surname);

		edp.setCellphonenumber(contactNumber);
		edp.setAltCellphonenumber(alternateContactNumber);
		edp.setEMail(email);
		edp.setPosition(currentPosition);
		edp.setZZ_ID_Passport_No(idNumber);
		if (age != null)
			edp.setAge(age);
		edp.setZZGender(gender);

		if (highestEducationId != null)
			edp.setZZ_LI_HighestEducation_ID(highestEducationId);
		edp.setZZ_NQF_Level(nqfLevel);

		edp.setisExecutive("EXECUTIVE".equalsIgnoreCase(executiveStatus));
		edp.setisAspiringExecutive("ASPIRING".equalsIgnoreCase(executiveStatus));

		edp.saveEx(trxName);
	}

	public void onIdNumberChanged()
	{
		if (idNumber == null)
		{
			age = null;
			gender = null;
			return;
		}

		String id = idNumber.trim();
		if (!id.matches("\\d{13}"))
		{
			age = null;
			gender = null;
			return;
		}

		int yy = Integer.parseInt(id.substring(0, 2));
		int mm = Integer.parseInt(id.substring(2, 4));
		int dd = Integer.parseInt(id.substring(4, 6));

		java.time.LocalDate today = java.time.LocalDate.now();
		int currentYY = today.getYear() % 100;
		int year = (yy <= currentYY ? 2000 + yy : 1900 + yy);

		try
		{
			java.time.LocalDate dob = java.time.LocalDate.of(year, mm, dd);
			age = java.time.Period.between(dob, today).getYears();
		}
		catch (Exception ex)
		{
			age = null;
		}

		int g = Character.getNumericValue(id.charAt(6)); // 7th digit
		gender = (g < 5) ? "F" : "M";

	}

	private void autofillFromLoggedInUserIfEmpty()
	{
		MUser u = new MUser(Env.getCtx(), Env.getAD_User_ID(Env.getCtx()), null);

		if (StringUtils.isBlank(firstName))
			this.firstName = u.getName();
		if (StringUtils.isBlank(surname))
			this.surname = u.getLastName();
		if (StringUtils.isBlank(idNumber))
		{
			Object v = u.getZZ_ID_Passport_No();
			if (v != null)
				this.idNumber = String.valueOf(v);
		}
		onIdNumberChanged();
	}

	private void loadHighestEducationOptionsFromTable()
	{
		highestEducationOptions.clear();

		List<X_ZZ_LI_HighestEducation> list = MasterUtil.getHighestEducations();
		for (X_ZZ_LI_HighestEducation he : list)
		{
			highestEducationOptions.add(new KeyNamePair(he.get_ID(), he.getName()));
		}

		selectedHighestEducation = highestEducationOptions.stream()
				.filter(x -> highestEducationId != null && x.getKey() == highestEducationId.intValue()).findFirst()
				.orElse(null);
	}

	private void loadNqfLevelOptionsFromReference()
	{
		nqfLevelOptions.clear();

		List<MRefList> refList = new Query(Env.getCtx(), MRefList.Table_Name,
				MRefList.COLUMNNAME_AD_Reference_ID + "=? AND " + MRefList.COLUMNNAME_IsActive + "='Y'", null)
						.setParameters(NQF_AD_REFERENCE_ID).setOrderBy(MRefList.COLUMNNAME_Value).list();

		for (MRefList r : refList)
		{
			nqfLevelOptions.add(new ValueNamePair(r.getValue(), r.getName()));
		}

		// keep selection in sync after reload
		selectedNqfLevel = nqfLevelOptions.stream().filter(x -> x.getValue().equals(nqfLevel)).findFirst().orElse(null);
	}

	@Override
	public boolean validateForm()
	{
		if (StringUtils.isNotBlank(idNumber) && !idNumber.trim().matches("\\d{13}"))
			throw new AdempiereException("ID number must be 13 digits.");

		if (StringUtils.isNotBlank(contactNumber) && !contactNumber.trim().matches("\\d{10}"))
			throw new AdempiereException("Cell number must be 10 digits.");

		if (StringUtils.isNotBlank(alternateContactNumber) && !alternateContactNumber.trim().matches("\\d{10}"))
			throw new AdempiereException("Alternate number must be 10 digits or empty.");

		if (StringUtils.isNotBlank(email) && !email.trim().matches("^[A-Za-z0-9+_.-]+@(.+)$"))
			throw new AdempiereException("Invalid email.");

		return true;
	}

	// --- Getters/Setters for ZK binding ---
	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getSurname()
	{
		return surname;
	}

	public void setSurname(String surname)
	{
		this.surname = surname;
	}

	public String getIdNumber()
	{
		return idNumber;
	}

	public void setIdNumber(String idNumber)
	{
		this.idNumber = idNumber;
	}

	public String getContactNumber()
	{
		return contactNumber;
	}

	public void setContactNumber(String contactNumber)
	{
		this.contactNumber = contactNumber;
	}

	public String getAlternateContactNumber()
	{
		return alternateContactNumber;
	}

	public void setAlternateContactNumber(String alternateContactNumber)
	{
		this.alternateContactNumber = alternateContactNumber;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getCurrentPosition()
	{
		return currentPosition;
	}

	public void setCurrentPosition(String currentPosition)
	{
		this.currentPosition = currentPosition;
	}

	public Integer getHighestEducationId()
	{
		return highestEducationId;
	}

	public void setHighestEducationId(Integer highestEducationId)
	{
		this.highestEducationId = highestEducationId;
	}

	public String getNqfLevel()
	{
		return nqfLevel;
	}

	public void setNqfLevel(String nqfLevel)
	{
		this.nqfLevel = nqfLevel;
	}

	public Integer getAge()
	{
		return age;
	}

	public void setAge(Integer age)
	{
		this.age = age;
	}

	public String getGender()
	{
		return gender;
	}

	public void setGender(String gender)
	{
		this.gender = gender;
	}

	public String getExecutiveStatus()
	{
		return executiveStatus;
	}

	public void setExecutiveStatus(String executiveStatus)
	{
		this.executiveStatus = executiveStatus;
	}

	public List<ValueNamePair> getNqfLevelOptions()
	{
		return nqfLevelOptions;
	}

	public ValueNamePair getSelectedNqfLevel()
	{
		return selectedNqfLevel;
	}

	public void setSelectedNqfLevel(ValueNamePair o)
	{
		this.selectedNqfLevel = o;
		this.nqfLevel = (o != null ? o.getValue() : null);
	}

	public List<KeyNamePair> getHighestEducationOptions()
	{
		return highestEducationOptions;
	}

	public KeyNamePair getSelectedHighestEducation()
	{
		return selectedHighestEducation;
	}

	public void setSelectedHighestEducation(KeyNamePair o)
	{
		this.selectedHighestEducation = o;
		this.highestEducationId = (o != null ? o.getKey() : null);
	}
}
