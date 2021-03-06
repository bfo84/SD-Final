/**
 * Autogenerated by Thrift Compiler (0.10.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package br.com.ufu.thrift.grafo;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.10.0)", date = "2017-11-09")
public class Vertice implements org.apache.thrift.TBase<Vertice, Vertice._Fields>, java.io.Serializable, Cloneable, Comparable<Vertice> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("Vertice");

  private static final org.apache.thrift.protocol.TField NOME_FIELD_DESC = new org.apache.thrift.protocol.TField("nome", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField COR_FIELD_DESC = new org.apache.thrift.protocol.TField("cor", org.apache.thrift.protocol.TType.I32, (short)2);
  private static final org.apache.thrift.protocol.TField DESC_FIELD_DESC = new org.apache.thrift.protocol.TField("desc", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField PESO_FIELD_DESC = new org.apache.thrift.protocol.TField("peso", org.apache.thrift.protocol.TType.DOUBLE, (short)4);
  private static final org.apache.thrift.protocol.TField BLOQUEADO_FIELD_DESC = new org.apache.thrift.protocol.TField("bloqueado", org.apache.thrift.protocol.TType.BOOL, (short)5);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new VerticeStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new VerticeTupleSchemeFactory();

  public int nome; // required
  public int cor; // required
  public java.lang.String desc; // required
  public double peso; // required
  public boolean bloqueado; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    NOME((short)1, "nome"),
    COR((short)2, "cor"),
    DESC((short)3, "desc"),
    PESO((short)4, "peso"),
    BLOQUEADO((short)5, "bloqueado");

    private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // NOME
          return NOME;
        case 2: // COR
          return COR;
        case 3: // DESC
          return DESC;
        case 4: // PESO
          return PESO;
        case 5: // BLOQUEADO
          return BLOQUEADO;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(java.lang.String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final java.lang.String _fieldName;

    _Fields(short thriftId, java.lang.String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public java.lang.String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __NOME_ISSET_ID = 0;
  private static final int __COR_ISSET_ID = 1;
  private static final int __PESO_ISSET_ID = 2;
  private static final int __BLOQUEADO_ISSET_ID = 3;
  private byte __isset_bitfield = 0;
  private static final _Fields optionals[] = {_Fields.BLOQUEADO};
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.NOME, new org.apache.thrift.meta_data.FieldMetaData("nome", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32        , "int")));
    tmpMap.put(_Fields.COR, new org.apache.thrift.meta_data.FieldMetaData("cor", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32        , "int")));
    tmpMap.put(_Fields.DESC, new org.apache.thrift.meta_data.FieldMetaData("desc", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.PESO, new org.apache.thrift.meta_data.FieldMetaData("peso", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.DOUBLE)));
    tmpMap.put(_Fields.BLOQUEADO, new org.apache.thrift.meta_data.FieldMetaData("bloqueado", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(Vertice.class, metaDataMap);
  }

  public Vertice() {
    this.bloqueado = false;

  }

  public Vertice(
    int nome,
    int cor,
    java.lang.String desc,
    double peso)
  {
    this();
    this.nome = nome;
    setNomeIsSet(true);
    this.cor = cor;
    setCorIsSet(true);
    this.desc = desc;
    this.peso = peso;
    setPesoIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Vertice(Vertice other) {
    __isset_bitfield = other.__isset_bitfield;
    this.nome = other.nome;
    this.cor = other.cor;
    if (other.isSetDesc()) {
      this.desc = other.desc;
    }
    this.peso = other.peso;
    this.bloqueado = other.bloqueado;
  }

  public Vertice deepCopy() {
    return new Vertice(this);
  }

  @Override
  public void clear() {
    setNomeIsSet(false);
    this.nome = 0;
    setCorIsSet(false);
    this.cor = 0;
    this.desc = null;
    setPesoIsSet(false);
    this.peso = 0.0;
    this.bloqueado = false;

  }

  public int getNome() {
    return this.nome;
  }

  public Vertice setNome(int nome) {
    this.nome = nome;
    setNomeIsSet(true);
    return this;
  }

  public void unsetNome() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __NOME_ISSET_ID);
  }

  /** Returns true if field nome is set (has been assigned a value) and false otherwise */
  public boolean isSetNome() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __NOME_ISSET_ID);
  }

  public void setNomeIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __NOME_ISSET_ID, value);
  }

  public int getCor() {
    return this.cor;
  }

  public Vertice setCor(int cor) {
    this.cor = cor;
    setCorIsSet(true);
    return this;
  }

  public void unsetCor() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __COR_ISSET_ID);
  }

  /** Returns true if field cor is set (has been assigned a value) and false otherwise */
  public boolean isSetCor() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __COR_ISSET_ID);
  }

  public void setCorIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __COR_ISSET_ID, value);
  }

  public java.lang.String getDesc() {
    return this.desc;
  }

  public Vertice setDesc(java.lang.String desc) {
    this.desc = desc;
    return this;
  }

  public void unsetDesc() {
    this.desc = null;
  }

  /** Returns true if field desc is set (has been assigned a value) and false otherwise */
  public boolean isSetDesc() {
    return this.desc != null;
  }

  public void setDescIsSet(boolean value) {
    if (!value) {
      this.desc = null;
    }
  }

  public double getPeso() {
    return this.peso;
  }

  public Vertice setPeso(double peso) {
    this.peso = peso;
    setPesoIsSet(true);
    return this;
  }

  public void unsetPeso() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __PESO_ISSET_ID);
  }

  /** Returns true if field peso is set (has been assigned a value) and false otherwise */
  public boolean isSetPeso() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __PESO_ISSET_ID);
  }

  public void setPesoIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __PESO_ISSET_ID, value);
  }

  public boolean isBloqueado() {
    return this.bloqueado;
  }

  public Vertice setBloqueado(boolean bloqueado) {
    this.bloqueado = bloqueado;
    setBloqueadoIsSet(true);
    return this;
  }

  public void unsetBloqueado() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __BLOQUEADO_ISSET_ID);
  }

  /** Returns true if field bloqueado is set (has been assigned a value) and false otherwise */
  public boolean isSetBloqueado() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __BLOQUEADO_ISSET_ID);
  }

  public void setBloqueadoIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __BLOQUEADO_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case NOME:
      if (value == null) {
        unsetNome();
      } else {
        setNome((java.lang.Integer)value);
      }
      break;

    case COR:
      if (value == null) {
        unsetCor();
      } else {
        setCor((java.lang.Integer)value);
      }
      break;

    case DESC:
      if (value == null) {
        unsetDesc();
      } else {
        setDesc((java.lang.String)value);
      }
      break;

    case PESO:
      if (value == null) {
        unsetPeso();
      } else {
        setPeso((java.lang.Double)value);
      }
      break;

    case BLOQUEADO:
      if (value == null) {
        unsetBloqueado();
      } else {
        setBloqueado((java.lang.Boolean)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case NOME:
      return getNome();

    case COR:
      return getCor();

    case DESC:
      return getDesc();

    case PESO:
      return getPeso();

    case BLOQUEADO:
      return isBloqueado();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case NOME:
      return isSetNome();
    case COR:
      return isSetCor();
    case DESC:
      return isSetDesc();
    case PESO:
      return isSetPeso();
    case BLOQUEADO:
      return isSetBloqueado();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof Vertice)
      return this.equals((Vertice)that);
    return false;
  }

  public boolean equals(Vertice that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_nome = true;
    boolean that_present_nome = true;
    if (this_present_nome || that_present_nome) {
      if (!(this_present_nome && that_present_nome))
        return false;
      if (this.nome != that.nome)
        return false;
    }

    boolean this_present_cor = true;
    boolean that_present_cor = true;
    if (this_present_cor || that_present_cor) {
      if (!(this_present_cor && that_present_cor))
        return false;
      if (this.cor != that.cor)
        return false;
    }

    boolean this_present_desc = true && this.isSetDesc();
    boolean that_present_desc = true && that.isSetDesc();
    if (this_present_desc || that_present_desc) {
      if (!(this_present_desc && that_present_desc))
        return false;
      if (!this.desc.equals(that.desc))
        return false;
    }

    boolean this_present_peso = true;
    boolean that_present_peso = true;
    if (this_present_peso || that_present_peso) {
      if (!(this_present_peso && that_present_peso))
        return false;
      if (this.peso != that.peso)
        return false;
    }

    boolean this_present_bloqueado = true && this.isSetBloqueado();
    boolean that_present_bloqueado = true && that.isSetBloqueado();
    if (this_present_bloqueado || that_present_bloqueado) {
      if (!(this_present_bloqueado && that_present_bloqueado))
        return false;
      if (this.bloqueado != that.bloqueado)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + nome;

    hashCode = hashCode * 8191 + cor;

    hashCode = hashCode * 8191 + ((isSetDesc()) ? 131071 : 524287);
    if (isSetDesc())
      hashCode = hashCode * 8191 + desc.hashCode();

    hashCode = hashCode * 8191 + org.apache.thrift.TBaseHelper.hashCode(peso);

    hashCode = hashCode * 8191 + ((isSetBloqueado()) ? 131071 : 524287);
    if (isSetBloqueado())
      hashCode = hashCode * 8191 + ((bloqueado) ? 131071 : 524287);

    return hashCode;
  }

  @Override
  public int compareTo(Vertice other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetNome()).compareTo(other.isSetNome());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetNome()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.nome, other.nome);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetCor()).compareTo(other.isSetCor());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCor()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.cor, other.cor);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetDesc()).compareTo(other.isSetDesc());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetDesc()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.desc, other.desc);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetPeso()).compareTo(other.isSetPeso());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPeso()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.peso, other.peso);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetBloqueado()).compareTo(other.isSetBloqueado());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetBloqueado()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.bloqueado, other.bloqueado);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    scheme(iprot).read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public java.lang.String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder("Vertice(");
    boolean first = true;

    sb.append("nome:");
    sb.append(this.nome);
    first = false;
    if (!first) sb.append(", ");
    sb.append("cor:");
    sb.append(this.cor);
    first = false;
    if (!first) sb.append(", ");
    sb.append("desc:");
    if (this.desc == null) {
      sb.append("null");
    } else {
      sb.append(this.desc);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("peso:");
    sb.append(this.peso);
    first = false;
    if (isSetBloqueado()) {
      if (!first) sb.append(", ");
      sb.append("bloqueado:");
      sb.append(this.bloqueado);
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class VerticeStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public VerticeStandardScheme getScheme() {
      return new VerticeStandardScheme();
    }
  }

  private static class VerticeStandardScheme extends org.apache.thrift.scheme.StandardScheme<Vertice> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, Vertice struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // NOME
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.nome = iprot.readI32();
              struct.setNomeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // COR
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.cor = iprot.readI32();
              struct.setCorIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // DESC
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.desc = iprot.readString();
              struct.setDescIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // PESO
            if (schemeField.type == org.apache.thrift.protocol.TType.DOUBLE) {
              struct.peso = iprot.readDouble();
              struct.setPesoIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // BLOQUEADO
            if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
              struct.bloqueado = iprot.readBool();
              struct.setBloqueadoIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, Vertice struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(NOME_FIELD_DESC);
      oprot.writeI32(struct.nome);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(COR_FIELD_DESC);
      oprot.writeI32(struct.cor);
      oprot.writeFieldEnd();
      if (struct.desc != null) {
        oprot.writeFieldBegin(DESC_FIELD_DESC);
        oprot.writeString(struct.desc);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(PESO_FIELD_DESC);
      oprot.writeDouble(struct.peso);
      oprot.writeFieldEnd();
      if (struct.isSetBloqueado()) {
        oprot.writeFieldBegin(BLOQUEADO_FIELD_DESC);
        oprot.writeBool(struct.bloqueado);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class VerticeTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public VerticeTupleScheme getScheme() {
      return new VerticeTupleScheme();
    }
  }

  private static class VerticeTupleScheme extends org.apache.thrift.scheme.TupleScheme<Vertice> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, Vertice struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetNome()) {
        optionals.set(0);
      }
      if (struct.isSetCor()) {
        optionals.set(1);
      }
      if (struct.isSetDesc()) {
        optionals.set(2);
      }
      if (struct.isSetPeso()) {
        optionals.set(3);
      }
      if (struct.isSetBloqueado()) {
        optionals.set(4);
      }
      oprot.writeBitSet(optionals, 5);
      if (struct.isSetNome()) {
        oprot.writeI32(struct.nome);
      }
      if (struct.isSetCor()) {
        oprot.writeI32(struct.cor);
      }
      if (struct.isSetDesc()) {
        oprot.writeString(struct.desc);
      }
      if (struct.isSetPeso()) {
        oprot.writeDouble(struct.peso);
      }
      if (struct.isSetBloqueado()) {
        oprot.writeBool(struct.bloqueado);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, Vertice struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(5);
      if (incoming.get(0)) {
        struct.nome = iprot.readI32();
        struct.setNomeIsSet(true);
      }
      if (incoming.get(1)) {
        struct.cor = iprot.readI32();
        struct.setCorIsSet(true);
      }
      if (incoming.get(2)) {
        struct.desc = iprot.readString();
        struct.setDescIsSet(true);
      }
      if (incoming.get(3)) {
        struct.peso = iprot.readDouble();
        struct.setPesoIsSet(true);
      }
      if (incoming.get(4)) {
        struct.bloqueado = iprot.readBool();
        struct.setBloqueadoIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

