# AD Changes For EDP Application

---

## System Side

Created a new **Menu** with predefined context variables
- **Menu**
  - **Name:** EDP Application by Individuals
  - **Predefined Context Variables:**
  
    ZZ_Program_Master_Data_UU = c3eb1942-c79d-4208-93e1-b99ee38a73f6
  
    programType              = EDP_APP_INDIVIDUAL

- **Reference Configuration:**
  - Added a new **Program Type** in the reference list
    - **Reference Name:** Program Type
    - **Validation Type:** List Validation
    - **New Value:** EDP_APP_INDIVIDUAL

---

## Client Side

- Created a new **Project** :   **Executive Development Program**

- Created **Program Definition** :   **EDP Application by Individuals**

- Registered the program in the **Open Application window**

---

## 2Pack Deployment

AD changes for System + Client side:

  ### System 2Pack
  - \2pack\SYSTEM_20260126_za.co.ntier.webform.EDP_Individual.zip
  
  ### Client 2Pack
  - \2pack\CLIENT_MQA_20260126_za.co.ntier.webform.EDP_Individual.zip

---
