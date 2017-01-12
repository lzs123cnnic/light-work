package com.baosight.iwater.bigdata.userdata;

import java.text.DecimalFormat;

import com.baosight.iwater.bigdata.StringUtils;

/**
 * 查询水位参数：水位仪表数量有 N 个，每个水位值 4 字节，取值范围为-9999.999～+9999.999， 单位为 m，
 * 数据域 4*N＋4 字节。数据格式见表 85。  
 * @author chaos
 * @email xuzhengchao@baosight.com
 * @date 2017年1月12日下午5:09:04
 */
public class ShuiweiData extends AbstractSelfReportData {
	
	public static final int CFN_CODE = 2;
	private DecimalFormat df = new DecimalFormat("######0.000");   	
	private double value;

	public ShuiweiData(double value) {
		super();
		this.value = value;
	}

	@Override
	public int getCFNCode() {
		// TODO Auto-generated method stub
		return CFN_CODE;
	}

	@Override
	public String getData() throws Exception {		
		if(Double.compare(value, 99999.9)>0||Double.compare(value, -99999.9)<0){
			throw new Exception("数值超出上限值99999.9或超出下限值-99999.9!");
		}
		boolean isMinus = Double.compare(value, 0)<0?true:false;
		String formatStr = df.format(Math.abs(this.value));		
		String str = StringUtils.getFixLengthString(formatStr, 8);
		String byte1 = StringUtils.getPosFixLengthString(str,6,4)+ StringUtils.getPosFixLengthString(str,7,4);
		String byte2 = StringUtils.getPosFixLengthString(str,3,4)+ StringUtils.getPosFixLengthString(str,5,4);
		String byte3 = StringUtils.getPosFixLengthString(str,1,4)+ StringUtils.getPosFixLengthString(str,2,4);	
		String mstr = "0000";
		if(isMinus){
			mstr = "1111";
		}
		String byte4 = mstr+ StringUtils.getPosFixLengthString(str,0,4);	
		return AFN_CODE+byte1+byte2+byte3+byte4;
	}
	
	public static void main(String args[]) {
		try {
			System.out.println(new ShuiweiData(-20.2).getData());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
