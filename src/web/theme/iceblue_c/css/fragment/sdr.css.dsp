/**** new grid system ****/

.mqaWebForm .container .formBottomPanel{
  padding: 10px;
  padding-right: 20px;
  padding-left: 0px;
  display: flex;
  justify-content: space-between;
}

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
  overflow-y:auto !important;
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

.mqaWebForm .sdrForm .orglink,
.mqaWebForm .sdrForm .bankDetails{
  grid-template-columns: 1fr;
}

.mqaWebForm .sdrForm .bankDetails > :nth-child(2) .label{
  color:red;
}

.mqaWebForm .sdrForm .bankDetails > :nth-child(2) .cell-title{
  background-color:unset;
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

.mqaWebForm .grid-formview .cell {
    /* FLEXBOX STACKING & SPACE CONTROL */
    display: flex;
    flex-direction: column; 
    width: 100%;
    height: 100%; 
    justify-content: flex-end;
    padding:5px;
}

.mqaWebForm .grid-formview .z-select{
  background:white;
}

.mqaWebForm .grid-formview .cell-title {
    /* TITLE AREA FLEXBOX (CRUCIAL FIX) */
    display: flex;
    flex-wrap: wrap; 
    align-items: flex-end; 
    flex-shrink: 0; 
    background:var(--mqa-blue);
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

.mqaWebForm .grid-formview.orglink .fileUploadContent,
.mqaWebForm .grid-formview.bankDetails .fileUploadContent{
  align-items:start;
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
}

.mqaWebForm .sdrForm .grid-listView.grid-listView-7{
  grid-template-columns: repeat(7, 1fr);
}

.mqaWebForm .sdrForm .grid-listView.grid-listView-3{
  grid-template-columns: repeat(3, 1fr);
}