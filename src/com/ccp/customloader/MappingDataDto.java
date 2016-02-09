package com.ccp.customloader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pravanga on 5/19/2015.
 */
public class MappingDataDto {
    int custId;
    String collId;
    String orderList;
    String delimiter;
    int dataStartLine;
    int headerStartLine;
    String applicationType;
    String custType;


	List<FieldMappingDto> fieldMappingDtoList;
    int numberOfFields;

    public MappingDataDto() {
        fieldMappingDtoList = new ArrayList<FieldMappingDto>();
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getCollId() {
        return collId;
    }

    public void setCollId(String collId) {
        this.collId = collId;
    }
    
	public int getHeaderStartLine() {
		return headerStartLine;
	}

	public void setHeaderStartLine(int headerStartLine) {
		this.headerStartLine = headerStartLine;
	}

    public List<FieldMappingDto> getFieldMappingDtoList() {
        return fieldMappingDtoList;
    }

    public void setFieldMappingDtoList(List<FieldMappingDto> fieldMappingDtoList) {
        this.fieldMappingDtoList = fieldMappingDtoList;
    }

    public String getOrderList() {
        return orderList;
    }

    public void setOrderList(String orderList) {
        this.orderList = orderList;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    
    public String getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}
	
	public String getCustType() {
		return custType;
	}

	public void setCustType(String custType) {
		this.custType = custType;
	}

    public int getDataStartLine() {
        return dataStartLine;
    }

    public void setDataStartLine(int dataStartLine) {
        this.dataStartLine = dataStartLine;
    }

    public int getNumberOfFields() {
        return numberOfFields;
    }

    public void setNumberOfFields(int numberOfFields) {
        this.numberOfFields = numberOfFields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MappingDataDto that = (MappingDataDto) o;

        if (custId != that.custId) return false;
        if (dataStartLine != that.dataStartLine) return false;
        if (headerStartLine != that.headerStartLine) return false;
        if (applicationType != null ? !applicationType.equals(that.applicationType) : that.applicationType != null)
            return false;
        if (collId != null ? !collId.equals(that.collId) : that.collId != null) return false;
        if (custType != null ? !custType.equals(that.custType) : that.custType != null) return false;
        if (delimiter != null ? !delimiter.equals(that.delimiter) : that.delimiter != null) return false;
        if (fieldMappingDtoList != null ? !fieldMappingDtoList.equals(that.fieldMappingDtoList) : that.fieldMappingDtoList != null)
            return false;
        if (orderList != null ? !orderList.equals(that.orderList) : that.orderList != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = custId;
        result = 31 * result + (collId != null ? collId.hashCode() : 0);
        result = 31 * result + (orderList != null ? orderList.hashCode() : 0);
        result = 31 * result + (delimiter != null ? delimiter.hashCode() : 0);
        result = 31 * result + dataStartLine;
        result = 31 * result + headerStartLine;
        result = 31 * result + (applicationType != null ? applicationType.hashCode() : 0);
        result = 31 * result + (custType != null ? custType.hashCode() : 0);
        result = 31 * result + (fieldMappingDtoList != null ? fieldMappingDtoList.hashCode() : 0);
        return result;
    }
}
