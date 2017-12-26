/**
 * Autogenerated by Thrift Compiler (0.10.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package br.com.ufu.thrift.graph;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.10.0)", date = "2017-11-09")
public class Graph implements org.apache.thrift.TBase<Graph, Graph._Fields>, java.io.Serializable, Cloneable, Comparable<Graph> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("Graph");

  private static final org.apache.thrift.protocol.TField VERTICES_FIELD_DESC = new org.apache.thrift.protocol.TField("vertices", org.apache.thrift.protocol.TType.MAP, (short)1);
  private static final org.apache.thrift.protocol.TField ARESTAS_FIELD_DESC = new org.apache.thrift.protocol.TField("arestas", org.apache.thrift.protocol.TType.MAP, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new GraphStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new GraphTupleSchemeFactory();

  public java.util.Map<java.lang.Integer,Vertex> vertices; // required
  public java.util.Map<Id,Edge> arestas; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    VERTICES((short)1, "vertices"),
    ARESTAS((short)2, "arestas");

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
        case 1: // VERTICES
          return VERTICES;
        case 2: // ARESTAS
          return ARESTAS;
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
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.VERTICES, new org.apache.thrift.meta_data.FieldMetaData("vertices", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.MapMetaData(org.apache.thrift.protocol.TType.MAP, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32            , "int"), 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Vertex.class))));
    tmpMap.put(_Fields.ARESTAS, new org.apache.thrift.meta_data.FieldMetaData("arestas", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.MapMetaData(org.apache.thrift.protocol.TType.MAP, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Id.class), 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Edge.class))));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(Graph.class, metaDataMap);
  }

  public Graph() {
    this.vertices = new java.util.HashMap<java.lang.Integer,Vertex>();

    this.arestas = new java.util.HashMap<Id,Edge>();

  }

  public Graph(
    java.util.Map<java.lang.Integer,Vertex> vertices,
    java.util.Map<Id,Edge> arestas)
  {
    this();
    this.vertices = vertices;
    this.arestas = arestas;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Graph(Graph other) {
    if (other.isSetVertexs()) {
      java.util.Map<java.lang.Integer,Vertex> __this__vertices = new java.util.HashMap<java.lang.Integer,Vertex>(other.vertices.size());
      for (java.util.Map.Entry<java.lang.Integer, Vertex> other_element : other.vertices.entrySet()) {

        java.lang.Integer other_element_key = other_element.getKey();
        Vertex other_element_value = other_element.getValue();

        java.lang.Integer __this__vertices_copy_key = other_element_key;

        Vertex __this__vertices_copy_value = new Vertex(other_element_value);

        __this__vertices.put(__this__vertices_copy_key, __this__vertices_copy_value);
      }
      this.vertices = __this__vertices;
    }
    if (other.isSetEdges()) {
      java.util.Map<Id,Edge> __this__arestas = new java.util.HashMap<Id,Edge>(other.arestas.size());
      for (java.util.Map.Entry<Id, Edge> other_element : other.arestas.entrySet()) {

        Id other_element_key = other_element.getKey();
        Edge other_element_value = other_element.getValue();

        Id __this__arestas_copy_key = new Id(other_element_key);

        Edge __this__arestas_copy_value = new Edge(other_element_value);

        __this__arestas.put(__this__arestas_copy_key, __this__arestas_copy_value);
      }
      this.arestas = __this__arestas;
    }
  }

  public Graph deepCopy() {
    return new Graph(this);
  }

  @Override
  public void clear() {
    this.vertices = new java.util.HashMap<java.lang.Integer,Vertex>();

    this.arestas = new java.util.HashMap<Id,Edge>();

  }

  public int getVertexsSize() {
    return (this.vertices == null) ? 0 : this.vertices.size();
  }

  public void putToVertexs(int key, Vertex val) {
    if (this.vertices == null) {
      this.vertices = new java.util.HashMap<java.lang.Integer,Vertex>();
    }
    this.vertices.put(key, val);
  }

  public java.util.Map<java.lang.Integer,Vertex> getVertexs() {
    return this.vertices;
  }

  public Graph setVertexs(java.util.Map<java.lang.Integer,Vertex> vertices) {
    this.vertices = vertices;
    return this;
  }

  public void unsetVertexs() {
    this.vertices = null;
  }

  /** Returns true if field vertices is set (has been assigned a value) and false otherwise */
  public boolean isSetVertexs() {
    return this.vertices != null;
  }

  public void setVertexsIsSet(boolean value) {
    if (!value) {
      this.vertices = null;
    }
  }

  public int getEdgesSize() {
    return (this.arestas == null) ? 0 : this.arestas.size();
  }

  public void putToEdges(Id key, Edge val) {
    if (this.arestas == null) {
      this.arestas = new java.util.HashMap<Id,Edge>();
    }
    this.arestas.put(key, val);
  }

  public java.util.Map<Id,Edge> getEdges() {
    return this.arestas;
  }

  public Graph setEdges(java.util.Map<Id,Edge> arestas) {
    this.arestas = arestas;
    return this;
  }

  public void unsetEdges() {
    this.arestas = null;
  }

  /** Returns true if field arestas is set (has been assigned a value) and false otherwise */
  public boolean isSetEdges() {
    return this.arestas != null;
  }

  public void setEdgesIsSet(boolean value) {
    if (!value) {
      this.arestas = null;
    }
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case VERTICES:
      if (value == null) {
        unsetVertexs();
      } else {
        setVertexs((java.util.Map<java.lang.Integer,Vertex>)value);
      }
      break;

    case ARESTAS:
      if (value == null) {
        unsetEdges();
      } else {
        setEdges((java.util.Map<Id,Edge>)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case VERTICES:
      return getVertexs();

    case ARESTAS:
      return getEdges();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case VERTICES:
      return isSetVertexs();
    case ARESTAS:
      return isSetEdges();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof Graph)
      return this.equals((Graph)that);
    return false;
  }

  public boolean equals(Graph that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_vertices = true && this.isSetVertexs();
    boolean that_present_vertices = true && that.isSetVertexs();
    if (this_present_vertices || that_present_vertices) {
      if (!(this_present_vertices && that_present_vertices))
        return false;
      if (!this.vertices.equals(that.vertices))
        return false;
    }

    boolean this_present_arestas = true && this.isSetEdges();
    boolean that_present_arestas = true && that.isSetEdges();
    if (this_present_arestas || that_present_arestas) {
      if (!(this_present_arestas && that_present_arestas))
        return false;
      if (!this.arestas.equals(that.arestas))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetVertexs()) ? 131071 : 524287);
    if (isSetVertexs())
      hashCode = hashCode * 8191 + vertices.hashCode();

    hashCode = hashCode * 8191 + ((isSetEdges()) ? 131071 : 524287);
    if (isSetEdges())
      hashCode = hashCode * 8191 + arestas.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(Graph other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetVertexs()).compareTo(other.isSetVertexs());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetVertexs()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.vertices, other.vertices);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetEdges()).compareTo(other.isSetEdges());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetEdges()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.arestas, other.arestas);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("Graph(");
    boolean first = true;

    sb.append("vertices:");
    if (this.vertices == null) {
      sb.append("null");
    } else {
      sb.append(this.vertices);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("arestas:");
    if (this.arestas == null) {
      sb.append("null");
    } else {
      sb.append(this.arestas);
    }
    first = false;
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
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class GraphStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public GraphStandardScheme getScheme() {
      return new GraphStandardScheme();
    }
  }

  private static class GraphStandardScheme extends org.apache.thrift.scheme.StandardScheme<Graph> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, Graph struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // VERTICES
            if (schemeField.type == org.apache.thrift.protocol.TType.MAP) {
              {
                org.apache.thrift.protocol.TMap _map0 = iprot.readMapBegin();
                struct.vertices = new java.util.HashMap<java.lang.Integer,Vertex>(2*_map0.size);
                int _key1;
                Vertex _val2;
                for (int _i3 = 0; _i3 < _map0.size; ++_i3)
                {
                  _key1 = iprot.readI32();
                  _val2 = new Vertex();
                  _val2.read(iprot);
                  struct.vertices.put(_key1, _val2);
                }
                iprot.readMapEnd();
              }
              struct.setVertexsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // ARESTAS
            if (schemeField.type == org.apache.thrift.protocol.TType.MAP) {
              {
                org.apache.thrift.protocol.TMap _map4 = iprot.readMapBegin();
                struct.arestas = new java.util.HashMap<Id,Edge>(2*_map4.size);
                Id _key5;
                Edge _val6;
                for (int _i7 = 0; _i7 < _map4.size; ++_i7)
                {
                  _key5 = new Id();
                  _key5.read(iprot);
                  _val6 = new Edge();
                  _val6.read(iprot);
                  struct.arestas.put(_key5, _val6);
                }
                iprot.readMapEnd();
              }
              struct.setEdgesIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, Graph struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.vertices != null) {
        oprot.writeFieldBegin(VERTICES_FIELD_DESC);
        {
          oprot.writeMapBegin(new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.I32, org.apache.thrift.protocol.TType.STRUCT, struct.vertices.size()));
          for (java.util.Map.Entry<java.lang.Integer, Vertex> _iter8 : struct.vertices.entrySet())
          {
            oprot.writeI32(_iter8.getKey());
            _iter8.getValue().write(oprot);
          }
          oprot.writeMapEnd();
        }
        oprot.writeFieldEnd();
      }
      if (struct.arestas != null) {
        oprot.writeFieldBegin(ARESTAS_FIELD_DESC);
        {
          oprot.writeMapBegin(new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRUCT, org.apache.thrift.protocol.TType.STRUCT, struct.arestas.size()));
          for (java.util.Map.Entry<Id, Edge> _iter9 : struct.arestas.entrySet())
          {
            _iter9.getKey().write(oprot);
            _iter9.getValue().write(oprot);
          }
          oprot.writeMapEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class GraphTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public GraphTupleScheme getScheme() {
      return new GraphTupleScheme();
    }
  }

  private static class GraphTupleScheme extends org.apache.thrift.scheme.TupleScheme<Graph> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, Graph struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetVertexs()) {
        optionals.set(0);
      }
      if (struct.isSetEdges()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetVertexs()) {
        {
          oprot.writeI32(struct.vertices.size());
          for (java.util.Map.Entry<java.lang.Integer, Vertex> _iter10 : struct.vertices.entrySet())
          {
            oprot.writeI32(_iter10.getKey());
            _iter10.getValue().write(oprot);
          }
        }
      }
      if (struct.isSetEdges()) {
        {
          oprot.writeI32(struct.arestas.size());
          for (java.util.Map.Entry<Id, Edge> _iter11 : struct.arestas.entrySet())
          {
            _iter11.getKey().write(oprot);
            _iter11.getValue().write(oprot);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, Graph struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TMap _map12 = new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.I32, org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.vertices = new java.util.HashMap<java.lang.Integer,Vertex>(2*_map12.size);
          int _key13;
          Vertex _val14;
          for (int _i15 = 0; _i15 < _map12.size; ++_i15)
          {
            _key13 = iprot.readI32();
            _val14 = new Vertex();
            _val14.read(iprot);
            struct.vertices.put(_key13, _val14);
          }
        }
        struct.setVertexsIsSet(true);
      }
      if (incoming.get(1)) {
        {
          org.apache.thrift.protocol.TMap _map16 = new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRUCT, org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.arestas = new java.util.HashMap<Id,Edge>(2*_map16.size);
          Id _key17;
          Edge _val18;
          for (int _i19 = 0; _i19 < _map16.size; ++_i19)
          {
            _key17 = new Id();
            _key17.read(iprot);
            _val18 = new Edge();
            _val18.read(iprot);
            struct.arestas.put(_key17, _val18);
          }
        }
        struct.setEdgesIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}
