/**
用来获取后台下拉框数据
id 下拉框的id
dtype:数据类型
*/
function  getSelect(id,dtype){
	var m = $("#"+id).ligerComboBox({
		width : 180,
		url:"selectedQuery.action",
		parms:[{name: 'dictType', value: dtype}],
		isMultiSelect:false,
		valueField: 'id',
		selectBoxWidth: 180
	}); 
}
/** 系统配置表下拉框信息查询 */
function  getSelectConfig(id,dtype){
	var m = $("#"+id).ligerComboBox({
		width : 180,
		url:"selectedQueryConfig.action",
		parms:[{name: 'dictType', value: dtype}],
		isMultiSelect:false,
		valueField: 'id',
		selectBoxWidth: 180
	}); 
}
/** 流程名称下拉框信息查询 */
function  getSelectBusiness(id){
	var m = $("#"+id).ligerComboBox({
		width : 180,
		url:"selectedQueryBusiness.action",
		valueField: 'id',
		selectBoxWidth: 180
	}); 
}
/** 公司部门树下拉框信息查询 */
function  getSelectCorp(id){
	var m = $("#"+id).ligerComboBox({
		width : 180,
		selectBoxWidth: 180,
        selectBoxHeight: 400, 
        valueField: 'id',
        treeLeafOnly: false,//只能选中子节点的属性
        tree: { 
			url:"cropDeptTreeQuery.action", 
			ajaxType:'post',
			idFieldName: 'id',
			parentIDFieldName: 'pid',
			autoCheckboxEven:false,
			checkbox: false
		}
		
		
	}); 
}
