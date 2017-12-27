package com.aghit.task.algorithm.domain.exp;

import java.util.Map;
import java.util.Set;

import com.aghit.task.util.Constant;

/**
 * 多项目规则，右侧条件的表达式抽象类
 * 
 * @author Administrator
 * 
 */
public abstract class AbstractExpression {

	protected int type; // 表达式类型
	protected String operator; // 运算符：AND、OR，暂时只考虑AND
	//状态
	private boolean elem_state = false;
	//
	private Set<Long> preId;

	/**
	 * 评估逻辑表达式的值
	 * 
	 * @param refData 参考数据
	 * @return 逻辑表达式结果
	 */
	public abstract  Map<Boolean,Set<Long>> evaluate(Object[] refData);

	/**
	 * 返回条件表达式名称，用于以后日志输出等
	 * 
	 * @return 条件表达式名称
	 */
	public String getTypeLabel() {
		
		String label = "";
		// 判断表达式类型
		switch (this.type) {
		case Constant.CONDITION_PROJECT_DRUG:
			label = "药品";
			break;
		case Constant.CONDITION_PROJECT_DIAGNOSIS:
			label = "诊断";
			break;
		case Constant.CONDITION_PROJECT_TREAT:
			label = "诊疗";
			break;
		case Constant.CONDITION_PROJECT_CONSUM:
			label = "耗材";
			break;
		case Constant.CONDITION_PROJECT_HOSPITAL:
			label = "医院";
			break;
		case Constant.CONDITION_PROJECT_AGE:
			label = "年龄";
			break;
		case Constant.CONDITION_PROJECT_SEX:
			label = "性别";
			break;
		case Constant.CONDITION_PROJECT_PAY_TYPE:
			label = "支付类别";
			break;
		case Constant.CONDITION_PROJECT_DOCTOR_TITLE:
			label = "医师职称";
			break;
		case Constant.CONDITION_PROJECT_TIME_REPEAL:
			label = "时间重复性";
			break;
		case Constant.CONDITION_PROJECT_OVERDOSAGE:
			label = "超量审核";
			break;
//		case Constant.CONDITION_PROJECT_OVERFREQ:
//			label = "高频审核";
//			break;
		case Constant.CONDITION_PROJECT_HOSP_RANK:
			label = "医院等级";
			break;
		case Constant.CONDITION_PROJECT_INDICATION:
			label = "适用症";
			break;
		case Constant.CONDITION_PROJECT_CONTRAINDICATION:
			label = "禁忌症";
			break;
		case Constant.CONDITION_PROJECT_DDD:
			label = "合理日剂量";
			break;
		default:
			label = "不支持的类型";
		}
		return label;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public void setElem_state(boolean elem_state) {
		this.elem_state = elem_state;
	}
	public boolean isElem_state() {
		return elem_state;
	}

	public Set<Long> getPreId() {
		return preId;
	}

	public void setPreId(Set<Long> preId) {
		this.preId = preId;
	}
}
