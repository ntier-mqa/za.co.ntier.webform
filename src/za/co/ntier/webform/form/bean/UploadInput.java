package za.co.ntier.webform.form.bean;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class UploadInput extends AnnexureInfo {
	
	public static UploadInput getUploadInput(int programMasterDataID) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		//query upload info
		
		List<ColumnInfo<?>> columns = new ArrayList<>();
		columns.add(ColumnInfo.getColLabel("Document Name"));
		columns.add(ColumnInfo.getColFileUpload("", "doc"));
		
		UploadInput uploadInput = AnnexureInfo.getAnnexureInfo(UploadInput.class, 
				columns, false);
		
		Map<ColumnInfo<?>, Object> rowDataInits = new HashMap<>();
		
		Random random = new Random();
		int min = 3;
        int max = 10;
        int randomNumber = random.nextInt((max - min) + 1) + min;
        
		for (int num = 0; num < randomNumber; num++) {
			rowDataInits.put(columns.get(0), "document " + num);
			Map<ColumnInfo<?>, Object> newRow = AnnexureInfo.createDetailRow(columns, rowDataInits);
			uploadInput.getRows().add(newRow);
		}
		return uploadInput;
	}
}
