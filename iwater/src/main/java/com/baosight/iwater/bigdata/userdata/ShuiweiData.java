package com.baosight.iwater.bigdata.userdata;

import java.text.DecimalFormat;

import org.apache.log4j.Logger;

import com.baosight.iwater.bigdata.StringUtils;

/**
 * 查询水位参数：水位仪表数量有 N 个，每个水位值 4 字节，取值范围为-9999.999～+9999.999， 单位为 m，
 * 数据域 4*N＋4 字节。数据格式见表 85。  
 * @author chaos
 * @email xuzhengchao@baosight.com
 * @date 2017年1月12日下午5:09:04
 */
public class ShuiweiData extends AbstractSelfReportData {
	private static Logger logger = Logger.getLogger(ShuiweiData.class);
	
	public static final int CFN_CODE = 2;
	private DecimalFormat df = new DecimalFormat("######0.000");   	

	public ShuiweiData(double value) {
		super(value);
	}

	@Override
	public int getCFNCode() {
		// TODO Auto-generated method stub
		logger.info("生成水位消息数据！");
		return CFN_CODE;
	}

	@Override
	public String getData() throws Exception {		
		if(Double.compare(value, 9999.999)>0||Double.compare(value, -9999.999)<0){
			throw new Exception("数值超出上限值9999.999或超出下限值-9999.999!");
		}
		boolean isMinus = Double.compare(value, 0)<0?true:false;
		String formatStr = df.format(Math.abs(this.value));		
		String str = StringUtils.getFixLengthString(formatStr, 8);
		String byte1 = StringUtils.getPosHexString(str,6,1)+ StringUtils.getPosHexString(str,7,1);
		String byte2 = StringUtils.getPosHexString(str,3,1)+ StringUtils.getPosHexString(str,5,1);
		String byte3 = StringUtils.getPosHexString(str,1,1)+ StringUtils.getPosHexString(str,2,1);	
		String mstr = "0";
		if(isMinus){
			mstr = "F";
		}
		String byte4 = mstr+" "+ StringUtils.getPosHexString(str,0,1);	
		
		String data = byte1+" "+byte2+" "+byte3+" "+byte4;
		logger.info("水位数值："+value+",报文值："+data);
		return this.getAFNCode()+" "+data;
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
