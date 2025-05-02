package Model;

import DataAccess.GetSrvdefpf.SrvDefInfo;

public class CardInfo {
	private int customerNo;
	private String product;
	private int cardLimit;
	private long cardNumber;
	private int applLopn;
	private int accNumber;
	private int accCat;
	private int grpAccNumber;
	private String subAccType;
	private int spgrpAccNumber;

	SrvDefInfo info; // from GetSrvdefpf

	// Constructor with char for cardNumber
	public CardInfo(int customerNo, String product, int cardLimit) {
		this.customerNo = customerNo;
		this.product = product;
		this.cardLimit = cardLimit;
		}

	public int getCustomerNo() {
		return customerNo;
	}

	public String getProduct() {
		return product;
	}

	public int getCardLimit() {
		return cardLimit;
	}

	public long getCardNumber() {
		return cardNumber;
	}

	public int getApplLopn() {
		return applLopn;
	}

	public int getAccNumber() {
		return accNumber;
	}

	public int getAccCat() {
		return accCat;
	}

	public int getGrpAccNumber() {
		return grpAccNumber;
	}

	public String getSubAccType() {
		return subAccType;
	}

	public int getSpgrpAccNumber() {
		return spgrpAccNumber;
	}

	public SrvDefInfo getInfo() {
		return info;
	}

	public void setCustomerNo(int customerNo) {
		this.customerNo = customerNo;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public void setCardLimit(int cardLimit) {
		this.cardLimit = cardLimit;
	}

	public void setCardNumber(long cardNumber) {
		this.cardNumber = cardNumber;
	}

	public void setApplLopn(int applLopn) {
		this.applLopn = applLopn;
	}

	public void setAccNumber(int accNumber) {
		this.accNumber = accNumber;
	}

	public void setAccCat(int accCat) {
		this.accCat = accCat;
	}

	public void setGrpAccNumber(int grpAccNumber) {
		this.grpAccNumber = grpAccNumber;
	}

	public void setSubAccType(String subAccType) {
		this.subAccType = subAccType;
	}

	public void setSpgrpAccNumber(int spgrpAccNumber) {
		this.spgrpAccNumber = spgrpAccNumber;
	}

	public void setInfo(SrvDefInfo info) {
		this.info = info;
	}

}