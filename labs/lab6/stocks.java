// ORM class for table 'stocks'
// WARNING: This class is AUTO-GENERATED. Modify at your own risk.
//
// Debug information:
// Generated date: Tue Apr 07 01:42:21 UTC 2020
// For connector: org.apache.sqoop.manager.MySQLManager
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.lib.db.DBWritable;
import com.cloudera.sqoop.lib.JdbcWritableBridge;
import com.cloudera.sqoop.lib.DelimiterSet;
import com.cloudera.sqoop.lib.FieldFormatter;
import com.cloudera.sqoop.lib.RecordParser;
import com.cloudera.sqoop.lib.BooleanParser;
import com.cloudera.sqoop.lib.BlobRef;
import com.cloudera.sqoop.lib.ClobRef;
import com.cloudera.sqoop.lib.LargeObjectLoader;
import com.cloudera.sqoop.lib.SqoopRecord;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class stocks extends SqoopRecord  implements DBWritable, Writable {
  private final int PROTOCOL_VERSION = 3;
  public int getClassFormatVersion() { return PROTOCOL_VERSION; }
  protected ResultSet __cur_result_set;
  private Integer id;
  public Integer get_id() {
    return id;
  }
  public void set_id(Integer id) {
    this.id = id;
  }
  public stocks with_id(Integer id) {
    this.id = id;
    return this;
  }
  private String symbol;
  public String get_symbol() {
    return symbol;
  }
  public void set_symbol(String symbol) {
    this.symbol = symbol;
  }
  public stocks with_symbol(String symbol) {
    this.symbol = symbol;
    return this;
  }
  private java.sql.Date quote_date;
  public java.sql.Date get_quote_date() {
    return quote_date;
  }
  public void set_quote_date(java.sql.Date quote_date) {
    this.quote_date = quote_date;
  }
  public stocks with_quote_date(java.sql.Date quote_date) {
    this.quote_date = quote_date;
    return this;
  }
  private java.math.BigDecimal open_price;
  public java.math.BigDecimal get_open_price() {
    return open_price;
  }
  public void set_open_price(java.math.BigDecimal open_price) {
    this.open_price = open_price;
  }
  public stocks with_open_price(java.math.BigDecimal open_price) {
    this.open_price = open_price;
    return this;
  }
  private java.math.BigDecimal high_price;
  public java.math.BigDecimal get_high_price() {
    return high_price;
  }
  public void set_high_price(java.math.BigDecimal high_price) {
    this.high_price = high_price;
  }
  public stocks with_high_price(java.math.BigDecimal high_price) {
    this.high_price = high_price;
    return this;
  }
  private java.math.BigDecimal low_price;
  public java.math.BigDecimal get_low_price() {
    return low_price;
  }
  public void set_low_price(java.math.BigDecimal low_price) {
    this.low_price = low_price;
  }
  public stocks with_low_price(java.math.BigDecimal low_price) {
    this.low_price = low_price;
    return this;
  }
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof stocks)) {
      return false;
    }
    stocks that = (stocks) o;
    boolean equal = true;
    equal = equal && (this.id == null ? that.id == null : this.id.equals(that.id));
    equal = equal && (this.symbol == null ? that.symbol == null : this.symbol.equals(that.symbol));
    equal = equal && (this.quote_date == null ? that.quote_date == null : this.quote_date.equals(that.quote_date));
    equal = equal && (this.open_price == null ? that.open_price == null : this.open_price.equals(that.open_price));
    equal = equal && (this.high_price == null ? that.high_price == null : this.high_price.equals(that.high_price));
    equal = equal && (this.low_price == null ? that.low_price == null : this.low_price.equals(that.low_price));
    return equal;
  }
  public boolean equals0(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof stocks)) {
      return false;
    }
    stocks that = (stocks) o;
    boolean equal = true;
    equal = equal && (this.id == null ? that.id == null : this.id.equals(that.id));
    equal = equal && (this.symbol == null ? that.symbol == null : this.symbol.equals(that.symbol));
    equal = equal && (this.quote_date == null ? that.quote_date == null : this.quote_date.equals(that.quote_date));
    equal = equal && (this.open_price == null ? that.open_price == null : this.open_price.equals(that.open_price));
    equal = equal && (this.high_price == null ? that.high_price == null : this.high_price.equals(that.high_price));
    equal = equal && (this.low_price == null ? that.low_price == null : this.low_price.equals(that.low_price));
    return equal;
  }
  public void readFields(ResultSet __dbResults) throws SQLException {
    this.__cur_result_set = __dbResults;
    this.id = JdbcWritableBridge.readInteger(1, __dbResults);
    this.symbol = JdbcWritableBridge.readString(2, __dbResults);
    this.quote_date = JdbcWritableBridge.readDate(3, __dbResults);
    this.open_price = JdbcWritableBridge.readBigDecimal(4, __dbResults);
    this.high_price = JdbcWritableBridge.readBigDecimal(5, __dbResults);
    this.low_price = JdbcWritableBridge.readBigDecimal(6, __dbResults);
  }
  public void readFields0(ResultSet __dbResults) throws SQLException {
    this.id = JdbcWritableBridge.readInteger(1, __dbResults);
    this.symbol = JdbcWritableBridge.readString(2, __dbResults);
    this.quote_date = JdbcWritableBridge.readDate(3, __dbResults);
    this.open_price = JdbcWritableBridge.readBigDecimal(4, __dbResults);
    this.high_price = JdbcWritableBridge.readBigDecimal(5, __dbResults);
    this.low_price = JdbcWritableBridge.readBigDecimal(6, __dbResults);
  }
  public void loadLargeObjects(LargeObjectLoader __loader)
      throws SQLException, IOException, InterruptedException {
  }
  public void loadLargeObjects0(LargeObjectLoader __loader)
      throws SQLException, IOException, InterruptedException {
  }
  public void write(PreparedStatement __dbStmt) throws SQLException {
    write(__dbStmt, 0);
  }

  public int write(PreparedStatement __dbStmt, int __off) throws SQLException {
    JdbcWritableBridge.writeInteger(id, 1 + __off, 4, __dbStmt);
    JdbcWritableBridge.writeString(symbol, 2 + __off, 1, __dbStmt);
    JdbcWritableBridge.writeDate(quote_date, 3 + __off, 91, __dbStmt);
    JdbcWritableBridge.writeBigDecimal(open_price, 4 + __off, 3, __dbStmt);
    JdbcWritableBridge.writeBigDecimal(high_price, 5 + __off, 3, __dbStmt);
    JdbcWritableBridge.writeBigDecimal(low_price, 6 + __off, 3, __dbStmt);
    return 6;
  }
  public void write0(PreparedStatement __dbStmt, int __off) throws SQLException {
    JdbcWritableBridge.writeInteger(id, 1 + __off, 4, __dbStmt);
    JdbcWritableBridge.writeString(symbol, 2 + __off, 1, __dbStmt);
    JdbcWritableBridge.writeDate(quote_date, 3 + __off, 91, __dbStmt);
    JdbcWritableBridge.writeBigDecimal(open_price, 4 + __off, 3, __dbStmt);
    JdbcWritableBridge.writeBigDecimal(high_price, 5 + __off, 3, __dbStmt);
    JdbcWritableBridge.writeBigDecimal(low_price, 6 + __off, 3, __dbStmt);
  }
  public void readFields(DataInput __dataIn) throws IOException {
this.readFields0(__dataIn);  }
  public void readFields0(DataInput __dataIn) throws IOException {
    if (__dataIn.readBoolean()) { 
        this.id = null;
    } else {
    this.id = Integer.valueOf(__dataIn.readInt());
    }
    if (__dataIn.readBoolean()) { 
        this.symbol = null;
    } else {
    this.symbol = Text.readString(__dataIn);
    }
    if (__dataIn.readBoolean()) { 
        this.quote_date = null;
    } else {
    this.quote_date = new Date(__dataIn.readLong());
    }
    if (__dataIn.readBoolean()) { 
        this.open_price = null;
    } else {
    this.open_price = com.cloudera.sqoop.lib.BigDecimalSerializer.readFields(__dataIn);
    }
    if (__dataIn.readBoolean()) { 
        this.high_price = null;
    } else {
    this.high_price = com.cloudera.sqoop.lib.BigDecimalSerializer.readFields(__dataIn);
    }
    if (__dataIn.readBoolean()) { 
        this.low_price = null;
    } else {
    this.low_price = com.cloudera.sqoop.lib.BigDecimalSerializer.readFields(__dataIn);
    }
  }
  public void write(DataOutput __dataOut) throws IOException {
    if (null == this.id) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.id);
    }
    if (null == this.symbol) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    Text.writeString(__dataOut, symbol);
    }
    if (null == this.quote_date) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeLong(this.quote_date.getTime());
    }
    if (null == this.open_price) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    com.cloudera.sqoop.lib.BigDecimalSerializer.write(this.open_price, __dataOut);
    }
    if (null == this.high_price) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    com.cloudera.sqoop.lib.BigDecimalSerializer.write(this.high_price, __dataOut);
    }
    if (null == this.low_price) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    com.cloudera.sqoop.lib.BigDecimalSerializer.write(this.low_price, __dataOut);
    }
  }
  public void write0(DataOutput __dataOut) throws IOException {
    if (null == this.id) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeInt(this.id);
    }
    if (null == this.symbol) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    Text.writeString(__dataOut, symbol);
    }
    if (null == this.quote_date) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    __dataOut.writeLong(this.quote_date.getTime());
    }
    if (null == this.open_price) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    com.cloudera.sqoop.lib.BigDecimalSerializer.write(this.open_price, __dataOut);
    }
    if (null == this.high_price) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    com.cloudera.sqoop.lib.BigDecimalSerializer.write(this.high_price, __dataOut);
    }
    if (null == this.low_price) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    com.cloudera.sqoop.lib.BigDecimalSerializer.write(this.low_price, __dataOut);
    }
  }
  private static final DelimiterSet __outputDelimiters = new DelimiterSet((char) 44, (char) 10, (char) 0, (char) 0, false);
  public String toString() {
    return toString(__outputDelimiters, true);
  }
  public String toString(DelimiterSet delimiters) {
    return toString(delimiters, true);
  }
  public String toString(boolean useRecordDelim) {
    return toString(__outputDelimiters, useRecordDelim);
  }
  public String toString(DelimiterSet delimiters, boolean useRecordDelim) {
    StringBuilder __sb = new StringBuilder();
    char fieldDelim = delimiters.getFieldsTerminatedBy();
    __sb.append(FieldFormatter.escapeAndEnclose(id==null?"null":"" + id, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(symbol==null?"null":symbol, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(quote_date==null?"null":"" + quote_date, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(open_price==null?"null":open_price.toPlainString(), delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(high_price==null?"null":high_price.toPlainString(), delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(low_price==null?"null":low_price.toPlainString(), delimiters));
    if (useRecordDelim) {
      __sb.append(delimiters.getLinesTerminatedBy());
    }
    return __sb.toString();
  }
  public void toString0(DelimiterSet delimiters, StringBuilder __sb, char fieldDelim) {
    __sb.append(FieldFormatter.escapeAndEnclose(id==null?"null":"" + id, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(symbol==null?"null":symbol, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(quote_date==null?"null":"" + quote_date, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(open_price==null?"null":open_price.toPlainString(), delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(high_price==null?"null":high_price.toPlainString(), delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(low_price==null?"null":low_price.toPlainString(), delimiters));
  }
  private static final DelimiterSet __inputDelimiters = new DelimiterSet((char) 44, (char) 10, (char) 0, (char) 0, false);
  private RecordParser __parser;
  public void parse(Text __record) throws RecordParser.ParseError {
    if (null == this.__parser) {
      this.__parser = new RecordParser(__inputDelimiters);
    }
    List<String> __fields = this.__parser.parseRecord(__record);
    __loadFromFields(__fields);
  }

  public void parse(CharSequence __record) throws RecordParser.ParseError {
    if (null == this.__parser) {
      this.__parser = new RecordParser(__inputDelimiters);
    }
    List<String> __fields = this.__parser.parseRecord(__record);
    __loadFromFields(__fields);
  }

  public void parse(byte [] __record) throws RecordParser.ParseError {
    if (null == this.__parser) {
      this.__parser = new RecordParser(__inputDelimiters);
    }
    List<String> __fields = this.__parser.parseRecord(__record);
    __loadFromFields(__fields);
  }

  public void parse(char [] __record) throws RecordParser.ParseError {
    if (null == this.__parser) {
      this.__parser = new RecordParser(__inputDelimiters);
    }
    List<String> __fields = this.__parser.parseRecord(__record);
    __loadFromFields(__fields);
  }

  public void parse(ByteBuffer __record) throws RecordParser.ParseError {
    if (null == this.__parser) {
      this.__parser = new RecordParser(__inputDelimiters);
    }
    List<String> __fields = this.__parser.parseRecord(__record);
    __loadFromFields(__fields);
  }

  public void parse(CharBuffer __record) throws RecordParser.ParseError {
    if (null == this.__parser) {
      this.__parser = new RecordParser(__inputDelimiters);
    }
    List<String> __fields = this.__parser.parseRecord(__record);
    __loadFromFields(__fields);
  }

  private void __loadFromFields(List<String> fields) {
    Iterator<String> __it = fields.listIterator();
    String __cur_str = null;
    try {
    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.id = null; } else {
      this.id = Integer.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null")) { this.symbol = null; } else {
      this.symbol = __cur_str;
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.quote_date = null; } else {
      this.quote_date = java.sql.Date.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.open_price = null; } else {
      this.open_price = new java.math.BigDecimal(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.high_price = null; } else {
      this.high_price = new java.math.BigDecimal(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.low_price = null; } else {
      this.low_price = new java.math.BigDecimal(__cur_str);
    }

    } catch (RuntimeException e) {    throw new RuntimeException("Can't parse input data: '" + __cur_str + "'", e);    }  }

  private void __loadFromFields0(Iterator<String> __it) {
    String __cur_str = null;
    try {
    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.id = null; } else {
      this.id = Integer.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null")) { this.symbol = null; } else {
      this.symbol = __cur_str;
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.quote_date = null; } else {
      this.quote_date = java.sql.Date.valueOf(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.open_price = null; } else {
      this.open_price = new java.math.BigDecimal(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.high_price = null; } else {
      this.high_price = new java.math.BigDecimal(__cur_str);
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null") || __cur_str.length() == 0) { this.low_price = null; } else {
      this.low_price = new java.math.BigDecimal(__cur_str);
    }

    } catch (RuntimeException e) {    throw new RuntimeException("Can't parse input data: '" + __cur_str + "'", e);    }  }

  public Object clone() throws CloneNotSupportedException {
    stocks o = (stocks) super.clone();
    o.quote_date = (o.quote_date != null) ? (java.sql.Date) o.quote_date.clone() : null;
    return o;
  }

  public void clone0(stocks o) throws CloneNotSupportedException {
    o.quote_date = (o.quote_date != null) ? (java.sql.Date) o.quote_date.clone() : null;
  }

  public Map<String, Object> getFieldMap() {
    Map<String, Object> __sqoop$field_map = new TreeMap<String, Object>();
    __sqoop$field_map.put("id", this.id);
    __sqoop$field_map.put("symbol", this.symbol);
    __sqoop$field_map.put("quote_date", this.quote_date);
    __sqoop$field_map.put("open_price", this.open_price);
    __sqoop$field_map.put("high_price", this.high_price);
    __sqoop$field_map.put("low_price", this.low_price);
    return __sqoop$field_map;
  }

  public void getFieldMap0(Map<String, Object> __sqoop$field_map) {
    __sqoop$field_map.put("id", this.id);
    __sqoop$field_map.put("symbol", this.symbol);
    __sqoop$field_map.put("quote_date", this.quote_date);
    __sqoop$field_map.put("open_price", this.open_price);
    __sqoop$field_map.put("high_price", this.high_price);
    __sqoop$field_map.put("low_price", this.low_price);
  }

  public void setField(String __fieldName, Object __fieldVal) {
    if ("id".equals(__fieldName)) {
      this.id = (Integer) __fieldVal;
    }
    else    if ("symbol".equals(__fieldName)) {
      this.symbol = (String) __fieldVal;
    }
    else    if ("quote_date".equals(__fieldName)) {
      this.quote_date = (java.sql.Date) __fieldVal;
    }
    else    if ("open_price".equals(__fieldName)) {
      this.open_price = (java.math.BigDecimal) __fieldVal;
    }
    else    if ("high_price".equals(__fieldName)) {
      this.high_price = (java.math.BigDecimal) __fieldVal;
    }
    else    if ("low_price".equals(__fieldName)) {
      this.low_price = (java.math.BigDecimal) __fieldVal;
    }
    else {
      throw new RuntimeException("No such field: " + __fieldName);
    }
  }
  public boolean setField0(String __fieldName, Object __fieldVal) {
    if ("id".equals(__fieldName)) {
      this.id = (Integer) __fieldVal;
      return true;
    }
    else    if ("symbol".equals(__fieldName)) {
      this.symbol = (String) __fieldVal;
      return true;
    }
    else    if ("quote_date".equals(__fieldName)) {
      this.quote_date = (java.sql.Date) __fieldVal;
      return true;
    }
    else    if ("open_price".equals(__fieldName)) {
      this.open_price = (java.math.BigDecimal) __fieldVal;
      return true;
    }
    else    if ("high_price".equals(__fieldName)) {
      this.high_price = (java.math.BigDecimal) __fieldVal;
      return true;
    }
    else    if ("low_price".equals(__fieldName)) {
      this.low_price = (java.math.BigDecimal) __fieldVal;
      return true;
    }
    else {
      return false;    }
  }
}
