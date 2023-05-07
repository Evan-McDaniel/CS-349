package energyDashboard;

public class Customer {
	private int ID;
	private String name;
	private String phoneNumber;
	private String address;
	private double tariff;
	private String meterType;
	private double usage;
	/**
	 * @return the iD
	 */
	public int getID() {
		return ID;
	}
	/**
	 * @param iD the iD to set
	 */
	public void setID(int iD) {
		ID = iD;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the tariff
	 */
	public double getTariff() {
		return tariff;
	}
	/**
	 * @param tariff the tariff to set
	 */
	public void setTariff(double tariff) {
		this.tariff = tariff;
	}
	/**
	 * @return the meterType
	 */
	public String getMeterType() {
		return meterType;
	}
	/**
	 * @param meterType the meterType to set
	 */
	public void setMeterType(String meterType) {
		this.meterType = meterType;
	}
	public double getUsage() {
		return usage;
	}
	/**
	 * @param meterType the meterType to set
	 */
	public void setUsage(double usage) {
		this.usage = usage;
	}
	
	//constructor for customer
	public Customer(int iD, String name, String phoneNumber, String address, double tariff, String meterType, double usage) {
		ID = iD;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.tariff = tariff;
		this.meterType = meterType;
		this.usage = usage;
	}
	
	public Customer() {
		ID = 0;
		name = "";
		phoneNumber = "";
		address = "";
		tariff = 0;
		meterType = "";
		usage = 0;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	
}



