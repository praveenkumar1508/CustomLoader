package com.ccp.customloader;
/**
 * Created by pravanga on 5/19/2015.
 */
public class FieldMappingDto {
    int position;
    String outputField;
    String format;
    String type;

    public FieldMappingDto() {
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getOutputField() {
        return outputField;
    }

    public void setOutputField(String outputField) {
        this.outputField = outputField;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldMappingDto that = (FieldMappingDto) o;

        if (position != that.position) return false;
        if (format != null ? !format.equals(that.format) : that.format != null) return false;
        if (outputField != null ? !outputField.equals(that.outputField) : that.outputField != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = position;
        result = 31 * result + (outputField != null ? outputField.hashCode() : 0);
        result = 31 * result + (format != null ? format.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
