/**** new grid system ****/

/****** dialog *************/
.sdrDialog{
  display: flex;
  flex-direction: column;
  height: auto;
  max-height: 70%;
  max-width: 50%;
  
  .z-window-header{
    flex: 0 0 auto;
  }
  
  .z-window-content{
    flex: 1 1 auto;
    overflow-y: auto;
  }
  
  .msgs{
    display: flex;
    flex-direction: column;
  }
  
  &.ZZOrgLinksSubmitSuccess .msgs > :nth-child(1){
    font-weight: bold;
  }
}

.sdrDialog.ZZOrgLinksSubmitSuccess .msgs > :nth-child(1){
  font-weight: bold;
}
/****** for tab *************/
.mqaWebForm .sdrForm{
  height:100%; 
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
}

.mqaWebForm .sdrForm .flex-grow{
  flex:1;
  min-height:0;
}

.mqaWebForm .sdrForm .formBottomPanel{
  padding: 10px;
  padding-right: 20px;
  padding-left: 0px;
  display: flex;
  justify-content: space-between;
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
  overflow-y:auto !important;
  height:100%;
}

.mqaWebForm .sdrForm .srd-address-ctrl{
  grid-template-columns: 1fr;
} 

/***** bankDetails *****/
/*****************************/

.mqaWebForm .sdrForm .bankDetails{
    grid-template-columns: repeat(2, 1fr);
}

.mqaWebForm .sdrForm .bankDetails > :nth-child(1) .label{
  color:red;
}

.mqaWebForm .sdrForm .bankDetails > :nth-child(1) .cell-title{
  background-color:unset;
}

.mqaWebForm .sdrForm .bankDetails > :nth-child(2)
, .mqaWebForm .sdrForm .bankDetails > :nth-child(3){
  grid-column: span 2;
  width:50%;
}

.mqaWebForm .sdrForm .orgSearch{
  grid-template-columns: repeat(2, 1fr);
}
/***** organization link *****/
/*****************************/

.mqaWebForm .sdrForm .orglink{
    grid-template-columns: repeat(2, 1fr);
}

.mqaWebForm .sdrForm .orglink > :nth-child(1)
, .mqaWebForm .sdrForm .orglink > :nth-child(4)
, .mqaWebForm .sdrForm .orglink > :nth-child(5)
, .mqaWebForm .sdrForm .orglink > :nth-child(6){
  grid-column: span 2;
}

.mqaWebForm .sdrForm .orglink > :nth-child(4){
  width:50%;
}


/****** for tab *************/

.mqaWebForm .grid-formview {
    /* GRID LAYOUT */
    display: grid;
    grid-template-columns: repeat(3, 1fr); 
    padding: 20px 20px 20px 0;
    border-radius:8px;
    box-shadow:0 4px 6px rgba(0, 0, 0, 0.1);
    height:unset !important;/* reset element style add by include height=100%*/
}

.mqaWebForm .grid-formview.two-col {
  grid-template-columns: repeat(2, 1fr); 
}

.mqaWebForm .grid-formview.one-col{
 grid-template-columns: repeat(1, 1fr);
}

.mqaWebForm .sdrForm .nav-tab-panel.address .srd-address{
  grid-template-columns: repeat(6, 1fr); 
}

.mqaWebForm .sdrForm .nav-tab-panel.address .srd-address > :nth-child(n){
  grid-column: span 2;
}

.mqaWebForm .sdrForm .nav-tab-panel.address .srd-address > :nth-child(1)
,.mqaWebForm .sdrForm .nav-tab-panel.address .srd-address > :nth-child(2){
  grid-column: span 3;
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

.mqaWebForm .grid-formview .cell.cellType6{/*upload button*/
  justify-content: flex-start;
  
}

.mqaWebForm .grid-formview .z-select,
.mqaWebForm .sdrForm .z-select{
  background:white;
}

.mqaWebForm .grid-formview .cell-title {
    /* TITLE AREA FLEXBOX (CRUCIAL FIX) */
    /*display: flex;
    flex-wrap: wrap; 
    align-items: flex-end; */
    flex-shrink: 0; 
    background:var(--mqa-blue);
}

.mqaWebForm .grid-formview .cell-error {
	flex-grow:1;
}

.mqaWebForm .grid-formview .cell-error .z-grid-body .z-grid-emptybody td{
	height:0px;
}

.mqaWebForm .cell-error .z-grid{
	border-width:0px;
}

.mqaWebForm .grid-formview .cell-error .z-label
, .mqaWebForm .grid-listView .cell-error .z-label{
	color:red;
}

/* --- Styling for Label, Required, and Help Text (Adjusted margin-top) --- */

.mqaWebForm .grid-formview .cell-title .label {
  font-style:italic;
  color:white;
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
    margin-top: 0; 
}

/* --- Content Styling (Anchoring input to bottom, Unchanged) --- */

.mqaWebForm .grid-formview .cell-content {
    flex-shrink: 0;
    width: 100%;
}

.mqaWebForm .container .grid-container .form-field select,
.mqaWebForm .grid-formview .cell-content input[type="text"],
.mqaWebForm .grid-formview .cell-content input[type="tel"],
.mqaWebForm .grid-formview .cell-content .z-datebox-input,
.mqaWebForm .grid-formview .cell-content select,
.mqaWebForm .grid-formview .cell-content textarea,
.mqaWebForm .sdrForm input[type="text"],
.mqaWebForm .sdrForm select,
.mqaWebForm .sdrForm textarea{
    width: 100%; 
    padding: 8px 10px;
    box-sizing: border-box;
    /* Pushes the input to the bottom of its content area */
    margin-top: auto; 
}

.mqaWebForm .container .grid-container .form-field select
, .mqaWebForm .grid-formview .cell-content select
, .mqaWebForm .sdrForm select{
  padding-top:6px;
  padding-bottom:6px;
  border-width:1px;
}

.mqaWebForm .sdrForm input[type="text"],
.mqaWebForm .grid-formview .cell-content .z-textbox,
.mqaWebForm .grid-formview .cell-content .z-datebox-input,
.mqaWebForm .sdrForm .z-textbox,
.mqaWebForm .sdrForm .z-datebox-input{
  padding: 10px;
  height:34px;
}

.mqaWebForm .grid-formview .cell-content .z-datebox,
.mqaWebForm .sdrForm .z-datebox{
  height:34px;
}

.mqaWebForm .grid-formview .cell-content input[type="checkbox"],
.mqaWebForm .sdrForm input[type="checkbox"]{
  padding-top:5px;
  padding-left:5px;
  height:28px;
  width:28px;
  margin-left:5px
}

.mqaWebForm .grid-formview .cell-content .z-checkbox,
.mqaWebForm .sdrForm .z-checkbox{
  padding-bottom:2px;
  padding-top:3px;
}

.mqaWebForm .grid-formview .cell-content .z-datebox-icon,
.mqaWebForm .sdrForm .z-datebox-icon{
  font-size:24px;
}

/* container of content */
.mqaWebForm .fileUploadContent{
  display:flex;
  flex-direction:column;
  width:100%;
  align-items:center
}

.mqaWebForm .grid-formview.orglink .fileUploadContent,
.mqaWebForm .grid-formview.bankDetails .fileUploadContent{
  align-items:start;
}

/* customize per control */

/* person education */
.mqaWebForm .grid-formview.two-col.srd-education > :nth-child(7) {
  grid-column: span 2;
}

/* person detail */
.mqaWebForm .grid-formview.srd-person-detail > :nth-child(1){
  grid-column: span 3;
}

.mqaWebForm .grid-formview.srd-person-detail > :nth-child(1) .fileUploadContent
, .mqaWebForm .grid-formview.srd-address .checkboxContent {

  align-items:flex-start;
}

/* org general */
.mqaWebForm .grid-formview.srd-org-general{
	grid-template-columns: repeat(2, 1fr);
}

.mqaWebForm .grid-formview.srd-org-general > :nth-child(9){
  grid-column: span 2;
  width:50%;
}

/* org contact */
.mqaWebForm .grid-formview.srd-org-contact{
	grid-template-columns: repeat(2, 1fr);
}

.mqaWebForm .grid-formview.srd-org-contact > :nth-child(1){
  grid-column: span 2;
  width:50%;
}


.mqaWebForm .sdrForm .grid-listView.grid-listView-7{
  grid-template-columns: repeat(7, 1fr);
}

.mqaWebForm .sdrForm .grid-listView.grid-listView-3{
  grid-template-columns: repeat(3, 1fr);
}

/******* list view **********/
.mqaWebForm .sdrForm .grid-listView{
  display: grid;
  grid-auto-rows: min-content;
  gap: 1px;
  background-color: #e0e0e0;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  
}

.mqaWebForm .sdrForm .grid-listView .colHead{
	background-color:var(--mqa-blue);
	color:white;
	position: sticky;
	top: 0;
}

.mqaWebForm .sdrForm .grid-listView .colHead .z-label{
	color: var(--header-text-color);
}

.mqaWebForm .sdrForm .grid-listView .cell{
	padding: 5px;
}

.mqaWebForm .sdrForm .grid-listView.grid-listView-2{
	grid-template-columns: repeat(2, 1fr);
}

.mqaWebForm .sdrForm .grid-listView.grid-listView-7{
  grid-template-columns: repeat(7, 1fr);
}

.mqaWebForm .sdrForm .grid-listView.grid-listView-8{
  grid-template-columns: repeat(8, 1fr);
}

.mqaWebForm .sdrForm .grid-listView.grid-listView-3{
  grid-template-columns: repeat(3, 1fr);
}

.mqaWebForm .sdrForm a.z-button{
	text-decoration:none;
}

.mqaWebForm .nav-tab-panel .dgaAppInfo .z-checkbox{
	display: flex;
    flex-direction: row-reverse;
    align-items: center;
}  

.mqaWebForm .formContactCopy{
	margin-top: -45px;
}

.mqaWebForm .formContactCopy > :nth-child(1){
	grid-column: span 3;
	
}

.mqaWebForm .formContactCopy > :nth-child(1) .cell-content{
	display:flex;
  	justify-content:flex-end;
}

.outter-view_form.outter-orgSize{
	display: grid;
  	grid-template-columns: max-content 1fr;
}

.outter-view_form.outter-orgSize .tableTitle{
	align-content:center;
}


.mqaWebForm .grid-formview.orgSize{
	grid-column: span 2;
	width: 100%;
	grid-template-columns: repeat(2, 1fr);
}