:root {
    --header-parent-color: #4a90e2; /* A shade of blue */
    --header-text-color: #ffffff;  /* White */
    
    --header-child-color: color-mix(in srgb, var(--header-parent-color) 70%, white);
    --header-child-text-color: color-mix(in srgb, var(--header-parent-color) 40%, black);
    
}

/* WebContent/css/employer-form-styles.css */
.mqaWebForm {
	width: 100%; /* Full width of the parent container */
	height: 100%; /* Full height of the parent container */
	
}

.mqaWebForm .container {
	background-color: white; /* background for the form	*/	
    padding: 20px; /* Adds padding around the form content */
    border-radius: 8px; /* Rounded corners for the form container */
    box-shadow: 0 2px 4px rgba(0,0,0,0.1); /* Subtle shadow for depth */
    margin: 0 auto; /* Centers the form on the page */
    overflow: auto; /* Allows scrolling if content overflows */
	width: 100%; /* Full width of the parent container */
    height: 100%;
    max-width: 1050px;
}

.mqaWebForm .z-include{
    padding-bottom:10px;
    
}

.mqaWebForm .container .formCode {
    text-align: right;
    width: 100%;
    display: block;
    font-weight: bold;
    font-size: larger;	
    padding-right: 100px;
}

.mqaWebForm .container .formHeader {
    text-align: center;
    width: 100%;
    display: block;
    font-weight: bold;
    font-size: x-large;	
    padding: 5px 0 40px 0;
}

.mqaWebForm .container .sectionHeader {
    text-align: left;
    width: 100%;
    padding: 10px 0 5px 0;
}

.mqaWebForm .container .sectionHeaderTitle{
	font-weight: bold;
    font-size: larger;
}

.mqaWebForm .container .subSectionHeader,
.mqaWebForm .container .subSectionHeader.address {
    text-align: left;
    width: 100%;
    display: block;
    padding: 5px 20px 5px 0px;
}

.mqaWebForm .container .subSectionHeader.address {
	display:flex;
	justify-content: space-between;
}

.mqaWebForm .container .subSectionHeader.address button {
	
}

.mqaWebForm .container .subSectionHeaderTitle{
    font-weight: bold;
    font-size: smaller;
}

.mqaWebForm .container .generalAppRules li{
    margin-bottom: 10px; /* Space between list items */
    line-height: 1.5; /* Improved readability */
    
}

.mqaWebForm .z-checkbox-content{
	padding-left: 5px;
}

.mqaWebForm .sectionSeparator{
	padding-bottom: 15px;
}


.mqaWebForm .btn-circle {
    width: 35px;
    height: 35px;
    padding: 0;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 24px;
}

.mqaWebForm .btn-circle i{
	font-size: x-large;
}

.mqaWebForm .container .grid-container{
	display: grid;
    /* Create 3 equal-width columns */
    grid-template-columns: repeat(3, 1fr);
    /* Add space between columns and rows */
    //gap: 20px;
    padding: 20px 20px 20px 0;
    border-radius: 8px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    
    /* The parent container sets up the grid and its equal-height rows */
    align-items: stretch;
      
}


.mqaWebForm .container .grid-container .form-field{
	/* Make this a flexbox container to align the label and input vertically */
    display: flex;
    flex-direction: column;
    
    /* when a label so long break to multi lines, flex-end make label of short label and input component push to end, so still close together */
    justify-content: flex-end;
    
    padding: 5px;
}

.mqaWebForm .container .tabContainer{
	/* Make this a flexbox container to align the label and input vertically */
    display: flex;
    flex-direction: column;
    
    /* when a label so long break to multi lines, flex-end make label of short label and input component push to end, so still close together */
    justify-content: space-between;
    
    padding: 5px;
    height:100%;
}

.mqaWebForm .container .tabContainer .formContainer{
	overflow-y: auto;
	flex-grow:1
}

.mqaWebForm .container .formBottomPanel{
	padding: 10px;
	padding-right: 20px;
	padding-left: 0px;
	display: flex;
	justify-content: space-between;
}


.mqaWebForm .container .grid-container .form-field .label{
	/* Make this a flexbox container to align the label and input vertically */
    //font-weight: bold;
    font-style: italic;
    color: #555;

}

.mqaWebForm .container .grid-container .form-field .z-textbox,
.mqaWebForm .container .grid-container .form-field .z-datebox-input {
	padding: 10px;
	height:34px;
}

.mqaWebForm .container .grid-container .form-field .z-datebox{
	height:34px;
}

.mqaWebForm .container .grid-container .form-field input[type="checkbox"]{
	padding-top:5px;
	padding-left:5px;
	height:28px;
	width:28px;
	margin-left:5px

}

.mqaWebForm .container .grid-container .form-field .z-checkbox{
	padding-bottom:2px;
	padding-top:3px;
}


.mqaWebForm .container .grid-container .form-field .z-datebox-icon{
	font-size:24px;
}

.mqaWebForm .container .grid-container .form-field .z-select{
	padding:5px;
	border-width:1px;
}

.icon-on-right{
	display: flex;
    flex-direction: row-reverse;
    align-items: center;
}

.ellipsis {
  /* The key properties for the ellipsis effect */
    white-space: nowrap; /* Prevents the text from wrapping to the next line */
    overflow: hidden;     /* Hides any text that overflows the container */
    text-overflow: ellipsis; /* Displays "..." to indicate truncated text */
    display: block; /* Ensure the label is a block-level element */
}


/*** demo **/

  /* Section header style */
  .sectionHeaderTitle {
      background-color: #800000;
      color: #fff;
      padding: 6px 12px;
      border-radius: 4px;
      display: inline-block;
      font-weight: bold;
  }
  
 
  /* maroon pill-style subheader */
  .subSectionHeader .z-label.subSectionHeaderTitle,
  .z-label.subSectionHeaderTitle,
  .subSectionHeaderTitle {
    background:#800000;      /* maroon */
    color:#fff;              /* white text */
    padding:6px 12px;
    border-radius:4px;
    display:inline-block;    /* wraps just the text */
    font-weight:700;
  }

.mqaWebForm .grid-table,
.mqaWebForm .grid-form {
    display: grid;
    /* Let rows be automatically created with a minimum height */
    grid-auto-rows: minmax(40px, auto);
    gap: 1px; /* A subtle gap to create a bordered cell effect */
    background-color: #e0e0e0; /* A background to make the gaps look like borders */
    border-radius: 8px;
    overflow: hidden; /* Ensures rounded corners are respected */
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.mqaWebForm .grid-form {
	grid-template-columns: 1fr 1fr;
}

.mqaWebForm .grid-table-7{
	/* Define 3 columns with relative widths */
    grid-template-columns: 230px 100px 100px 100px 1fr 1fr 1fr;
}

.mqaWebForm .grid-table-6{
	/* Define 3 columns with relative widths */
    grid-template-columns: 230px 110px 110px 1fr 1fr 1fr;
}

.mqaWebForm .learningMaterial .grid-table-6{
	grid-template-columns: 1fr 1fr 1fr 1fr 1fr 1fr;

}

.mqaWebForm .grid-table-5{
	/* Define 3 columns with relative widths */
    grid-template-columns: 240px 130px 130px 1fr 1fr;
}

/* annexure */
.mqaWebForm .grid-table-4{
	/* Define 3 columns with relative widths */
    grid-template-columns: 1fr 1fr 1fr 1fr;
}

.mqaWebForm .grid-table-3{
	/* Define 3 columns with relative widths */
    grid-template-columns: 1fr 1fr 1fr;
}

.mqaWebForm .grid-table-2{
	/* Define 3 columns with relative widths */
    grid-template-columns: 1fr 1fr;
}

.mqaWebForm .grid-table-auto-2{
	/* Define 3 columns with relative widths */
    grid-template-columns: auto auto;
}

.mqaWebForm .grid-table-auto-3{
	/* Define 3 columns with relative widths */
    grid-template-columns: auto auto auto;
}

.mqaWebForm .grid-table-oneline-4{
	/* Define 3 columns with relative widths */
    grid-template-columns: 1fr 1fr 1fr 1fr;
}

/* --- Style for all grid items (cells) --- */
.mqaWebForm .grid-cell,
.mqaWebForm .grid-form-cell {
    padding: 5px;
    display: flex;
    align-items: center;
    justify-content: center;
    text-align: center;
}

.mqaWebForm .grid-form-cell{

}

.mqaWebForm .grid-cell input[type=text],
.mqaWebForm .grid-cell select,
.mqaWebForm .grid-cell span,
.mqaWebForm .grid-cell textarea, 
.mqaWebForm .grid-form-cell input[type=text],
.mqaWebForm .grid-form-cell select,
.mqaWebForm .grid-form-cell span,
.mqaWebForm .grid-form-cell textarea
{
	width:100%;
}

/* --- Header Cell Styling --- */
.mqaWebForm .grid-header-cell {
    background-color: var(--header-parent-color);
    font-weight: normal;
    /*text-transform: uppercase;*/
}

.mqaWebForm .grid-sub-header{
    background-color: var(--header-child-color);

}

.mqaWebForm .grid-header-cell .z-label{
	color: var(--header-text-color);
}

/* --- Detail Cell Styling --- */
.mqaWebForm .grid-detail-cell {
    background-color: #f7f7f7; /* Off-white for the main cells */
    color: #333;
    font-weight: normal;
}

/* Alternating row color for better readability */
.mqaWebForm .grid-detail-cell-2:nth-child(4n + 1),
.mqaWebForm .grid-detail-cell-2:nth-child(4n + 2),
.mqaWebForm .grid-detail-cell-3:nth-child(6n + 1),
.mqaWebForm .grid-detail-cell-3:nth-child(6n + 2),
.mqaWebForm .grid-detail-cell-3:nth-child(6n + 3),
.mqaWebForm .grid-detail-cell-4:nth-child(8n + 1),
.mqaWebForm .grid-detail-cell-4:nth-child(8n + 2),
.mqaWebForm .grid-detail-cell-4:nth-child(8n + 3),
.mqaWebForm .grid-detail-cell-4:nth-child(8n + 4),
.mqaWebForm .grid-detail-cell-5:nth-child(10n + 1),
.mqaWebForm .grid-detail-cell-5:nth-child(10n + 2),
.mqaWebForm .grid-detail-cell-5:nth-child(10n + 3),
.mqaWebForm .grid-detail-cell-5:nth-child(10n + 4),
.mqaWebForm .grid-detail-cell-5:nth-child(10n + 5),
.mqaWebForm .grid-detail-cell-6:nth-child(12n + 1),
.mqaWebForm .grid-detail-cell-6:nth-child(12n + 2),
.mqaWebForm .grid-detail-cell-6:nth-child(12n + 3),
.mqaWebForm .grid-detail-cell-6:nth-child(12n + 4),
.mqaWebForm .grid-detail-cell-6:nth-child(12n + 5),
.mqaWebForm .grid-detail-cell-6:nth-child(12n + 6),
.mqaWebForm .grid-detail-cell-7:nth-child(14n + 1),
.mqaWebForm .grid-detail-cell-7:nth-child(14n + 2),
.mqaWebForm .grid-detail-cell-7:nth-child(14n + 3),
.mqaWebForm .grid-detail-cell-7:nth-child(14n + 4),
.mqaWebForm .grid-detail-cell-7:nth-child(14n + 5),
.mqaWebForm .grid-detail-cell-7:nth-child(14n + 6),
.mqaWebForm .grid-detail-cell-7:nth-child(14n + 7)
{
    background-color: #ffffff; /* Pure white for contrast */
}

/* --- Footer Cell Styling --- */
.mqaWebForm .grid-footer-cell {
    background-color: var(--header-parent-color);
    color: var(--header-text-color);

    font-weight: bold;
}

.mqaWebForm .grid-sub-footer{
	background-color: var(--header-child-color);
}


.mqaWebForm .grid-footer-cell .z-label{
    color: var(--header-child-text-color);
}


.mqaWebFormRelate{
	display: flex;
	flex-direction: column;
	height: auto;
	max-height: 70%;
	max-width: 50%;
}

.mqaWebFormRelate .z-window-header{
	flex: 0 0 auto;
}

.mqaWebFormRelate .z-window-content{
	flex: 1 1 auto;
	overflow-y: auto;
}

.mqaWebFormRelate .msgs{
	display: flex;
	flex-direction: column;
}

.mqaWebFormRelate .msg{
	
}

/* only the star is red; everything else stays as before */
.mqaWebForm .grid-form-cell-header .required-star {
  color: #d0021b;   /* red */
  font-weight: 700; /* optional */
}


/* inline field error message */
.mqaWebForm .help-error {
  color: #d32f2f;      /* red */
  font-size: 12px;
  line-height: 1.2;
  margin-top: 4px;
  display: block;
}


/* highlight the native select wrapper when invalid */
.mqaWebForm .listbox-invalid .z-select {
  border-color: #d32f2f !important;
  box-shadow: 0 0 0 2px rgba(211, 47, 47, 0.15) inset;
}

/* ZK notification: error variant */
.z-notification-error {
  background: #d32f2f !important;
  border-color: #d32f2f !important;
  color: #fff !important;
}
.z-notification-error .z-notification-content,
.z-notification-error .z-notification-message {
  color: #fff !important;
}

/**** new grid system ****/

/****** for tab *************/
.mqaWebForm .sdrForm{
  height:100%; 
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
}

.mqaWebForm .sdrForm .navTabOuter{
  flex:1;
  display:flex;
  flex-direction:column;
  min-height:0;
}

.mqaWebForm .sdrForm .navTabOuter .z-tabbox-top{
  flex:1;
  display:flex;
  flex-direction:column;
  min-height:0;
  height:100%;
}

.mqaWebForm .sdrForm .navTabOuter .z-tabs{
  flex-shrink:0;
}
.mqaWebForm .sdrForm .navTabOuter .z-tabpanels{
  flex:1;
}

.mqaWebForm .sdrForm .navTabOuter .nav-tab-panel{
  overflow-y;auto;
  height:100%;
}

.mqaWebForm .sdrForm .navTabOuter .nav-tab-panel.address{
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-auto-rows: min-content;
}

.mqaWebForm .sdrForm .navTabOuter .nav-tab-panel.address > :first-child {
    grid-column: 1 / span 2;
}
/****** for tab *************/

.mqaWebForm .grid-formview {
    /* GRID LAYOUT */
    display: grid;
    grid-template-columns: repeat(3, 1fr); 
    padding: 20px 20px 20px 0;
    border-radius:8px;
    box-shadow:0 4px 6px rgba(0, 0, 0, 0.1);
}

.mqaWebForm .grid-formview.two-col {
  grid-template-columns: repeat(2, 1fr); 
}

.mqaWebForm .grid-formview.one-col{
 grid-template-columns: repeat(1, 1fr);
}

.mqaWebForm .grid-formview .cell {
    /* FLEXBOX STACKING & SPACE CONTROL */
    display: flex;
    flex-direction: column; 
    width: 100%;
    height: 100%; 
    justify-content: flex-end;
    padding:5px;
}

.mqaWebForm .grid-formview .cell-title {
    /* TITLE AREA FLEXBOX (CRUCIAL FIX) */
    display: flex;
    flex-wrap: wrap; 
    align-items: flex-end; 
    flex-shrink: 0; 
}

/* --- Styling for Label, Required, and Help Text (Adjusted margin-top) --- */

.mqaWebForm .grid-formview .cell-title .label {
	font-style:italic;
	color:#555;
    flex-shrink: 0; 
    margin-right: 5px; 
    text-transform: capitalize;
}

.mqaWebForm .grid-formview .cell-title .required {
    margin-right: 5px; 
    color: #cc0000;
    font-weight: bold;
}

.mqaWebForm .grid-formview .cell-title .help-text {
    flex-grow: 1; 
    font-size: 0.8em;
    font-weight: normal;
    color: #6a6a6a;
    line-height: 1.4;
    
    /* Remove the margin-top to ensure the text aligns cleanly with flex-end */
    margin-top: 0; 
}

/* --- Content Styling (Anchoring input to bottom, Unchanged) --- */

.mqaWebForm .grid-formview .cell-content {
    flex-shrink: 0;
    width: 100%;
}

.mqaWebForm .grid-formview .cell-content input[type="text"],
.mqaWebForm .grid-formview .cell-content select,
/* ... (all other inputs) ... */
.mqaWebForm .grid-formview .cell-content textarea {
    width: 100%; 
    padding: 8px 10px;
    box-sizing: border-box;
    /* Pushes the input to the bottom of its content area */
    margin-top: auto; 
}


.mqaWebForm .grid-formview .cell-content .z-textbox,
.mqaWebForm .grid-formview .cell-content .z-datebox-input{
	padding: 10px;
	height:34px;
}
.mqaWebForm .grid-formview .cell-content .z-datebox{
	height:34px;
}

.mqaWebForm .grid-formview .cell-content input[type="checkbox"]{
	padding-top:5px;
	padding-left:5px;
	height:28px;
	width:28px;
	margin-left:5px
}

.mqaWebForm .grid-formview .cell-content .z-checkbox{
	padding-bottom:2px;
	padding-top:3px;
}

.mqaWebForm .grid-formview .cell-content .z-datebox-icon{
	font-size:24px;
}

.mqaWebForm .grid-formview .cell-content .z-select{
	padding:5px;
	border-width:1px;
}

/* container of content */
.mqaWebForm .fileUploadContent{
  display:flex;
  flex-direction:column;
  width:100%;
  align-items:center
}

/* customize per control */
.mqaWebForm .grid-formview.two-col.srd-education > :nth-child(7) 
, .mqaWebForm .grid-formview.two-col.srd-address > :nth-child(1){
  grid-column: span 2;
}

.mqaWebForm .grid-formview.srd-person-detail > :nth-child(1){
  grid-column: span 3;
}

.mqaWebForm .grid-formview.srd-person-detail > :nth-child(1) .fileUploadContent
, .mqaWebForm .grid-formview.srd-address > :nth-child(1) .checkboxContent{

  align-items:flex-start;
}


