package iceyung.app.datasource;

/**
 * Enumeration of ClickHouse data types
 * Created by krash on 26.09.18.
 */
public enum ClickHouseDataType {
    // Unsigned
    UInt8,
    UInt16,
    UInt32,
    UInt64,

    // Signed
    Int8,
    Int16,
    Int32,
    Int64,

    // Floating point
    Float32,
    Float64,

    // Date time
    DateTime,
    Date,

    Decimal,

    // Misc
    String;

    String typo;

    ClickHouseDataType(String string) {
        this.typo = string;
    }

    ClickHouseDataType(){
        this.typo = this.toString();
    };

    public String getName(boolean isNullable, int precision, int scale) {
        String retval = this.typo;
        if (retval.equals("Decimal")){
            if (precision <= 9){
                retval = java.lang.String.format("Decimal32(%s)", scale);
            }
            else if (precision <= 18){
                retval = java.lang.String.format("Decimal64(%s)", scale);
            }
            else {
                retval = java.lang.String.format("Decimal128(%s)", scale);
            }
        }
        if (isNullable) {
            retval = "Nullable(" + retval + ")";
        }
        return retval;
    }
}
