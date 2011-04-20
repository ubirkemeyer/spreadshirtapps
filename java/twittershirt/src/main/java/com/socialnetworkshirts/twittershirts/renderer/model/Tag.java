package com.socialnetworkshirts.twittershirts.renderer.model;

/**
 * @author mbs
 * @version $version$
 */
public class Tag {
    private String value;
    private long count;

    public Tag() {
    }

    public Tag(String value, long count) {
        this.value = value;
        this.count = count;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        if (count != tag.count) return false;
        if (value != null ? !value.equals(tag.value) : tag.value != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (value != null ? value.hashCode() : 0);
        result = 31 * result + (int) (count ^ (count >>> 32));
        return result;
    }

    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("Tag{");
        sb.append("value='").append(value == null ? "null" : value).append("'");
        sb.append(", count=").append(count);
        sb.append('}');
        return sb.toString();
    }
}
