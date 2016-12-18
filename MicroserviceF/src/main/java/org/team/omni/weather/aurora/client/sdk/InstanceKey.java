/**
 * Autogenerated by Thrift Compiler (0.9.3)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.team.omni.weather.aurora.client.sdk;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
/**
 * A unique identifier for the active task within a job.
 */
@Generated(value = "Autogenerated by Thrift Compiler (0.9.3)", date = "2016-12-08")
public class InstanceKey implements org.apache.thrift.TBase<InstanceKey, InstanceKey._Fields>, java.io.Serializable, Cloneable, Comparable<InstanceKey> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("InstanceKey");

  private static final org.apache.thrift.protocol.TField JOB_KEY_FIELD_DESC = new org.apache.thrift.protocol.TField("jobKey", org.apache.thrift.protocol.TType.STRUCT, (short)1);
  private static final org.apache.thrift.protocol.TField INSTANCE_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("instanceId", org.apache.thrift.protocol.TType.I32, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new InstanceKeyStandardSchemeFactory());
    schemes.put(TupleScheme.class, new InstanceKeyTupleSchemeFactory());
  }

  /**
   * Key identifying the job.
   */
  public JobKey jobKey; // required
  /**
   * Unique instance ID for the active task in a job.
   */
  public int instanceId; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    /**
     * Key identifying the job.
     */
    JOB_KEY((short)1, "jobKey"),
    /**
     * Unique instance ID for the active task in a job.
     */
    INSTANCE_ID((short)2, "instanceId");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // JOB_KEY
          return JOB_KEY;
        case 2: // INSTANCE_ID
          return INSTANCE_ID;
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
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __INSTANCEID_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.JOB_KEY, new org.apache.thrift.meta_data.FieldMetaData("jobKey", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, JobKey.class)));
    tmpMap.put(_Fields.INSTANCE_ID, new org.apache.thrift.meta_data.FieldMetaData("instanceId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(InstanceKey.class, metaDataMap);
  }

  public InstanceKey() {
  }

  public InstanceKey(
    JobKey jobKey,
    int instanceId)
  {
    this();
    this.jobKey = jobKey;
    this.instanceId = instanceId;
    setInstanceIdIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public InstanceKey(InstanceKey other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetJobKey()) {
      this.jobKey = new JobKey(other.jobKey);
    }
    this.instanceId = other.instanceId;
  }

  public InstanceKey deepCopy() {
    return new InstanceKey(this);
  }

  @Override
  public void clear() {
    this.jobKey = null;
    setInstanceIdIsSet(false);
    this.instanceId = 0;
  }

  /**
   * Key identifying the job.
   */
  public JobKey getJobKey() {
    return this.jobKey;
  }

  /**
   * Key identifying the job.
   */
  public InstanceKey setJobKey(JobKey jobKey) {
    this.jobKey = jobKey;
    return this;
  }

  public void unsetJobKey() {
    this.jobKey = null;
  }

  /** Returns true if field jobKey is set (has been assigned a value) and false otherwise */
  public boolean isSetJobKey() {
    return this.jobKey != null;
  }

  public void setJobKeyIsSet(boolean value) {
    if (!value) {
      this.jobKey = null;
    }
  }

  /**
   * Unique instance ID for the active task in a job.
   */
  public int getInstanceId() {
    return this.instanceId;
  }

  /**
   * Unique instance ID for the active task in a job.
   */
  public InstanceKey setInstanceId(int instanceId) {
    this.instanceId = instanceId;
    setInstanceIdIsSet(true);
    return this;
  }

  public void unsetInstanceId() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __INSTANCEID_ISSET_ID);
  }

  /** Returns true if field instanceId is set (has been assigned a value) and false otherwise */
  public boolean isSetInstanceId() {
    return EncodingUtils.testBit(__isset_bitfield, __INSTANCEID_ISSET_ID);
  }

  public void setInstanceIdIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __INSTANCEID_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case JOB_KEY:
      if (value == null) {
        unsetJobKey();
      } else {
        setJobKey((JobKey)value);
      }
      break;

    case INSTANCE_ID:
      if (value == null) {
        unsetInstanceId();
      } else {
        setInstanceId((Integer)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case JOB_KEY:
      return getJobKey();

    case INSTANCE_ID:
      return getInstanceId();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case JOB_KEY:
      return isSetJobKey();
    case INSTANCE_ID:
      return isSetInstanceId();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof InstanceKey)
      return this.equals((InstanceKey)that);
    return false;
  }

  public boolean equals(InstanceKey that) {
    if (that == null)
      return false;

    boolean this_present_jobKey = true && this.isSetJobKey();
    boolean that_present_jobKey = true && that.isSetJobKey();
    if (this_present_jobKey || that_present_jobKey) {
      if (!(this_present_jobKey && that_present_jobKey))
        return false;
      if (!this.jobKey.equals(that.jobKey))
        return false;
    }

    boolean this_present_instanceId = true;
    boolean that_present_instanceId = true;
    if (this_present_instanceId || that_present_instanceId) {
      if (!(this_present_instanceId && that_present_instanceId))
        return false;
      if (this.instanceId != that.instanceId)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_jobKey = true && (isSetJobKey());
    list.add(present_jobKey);
    if (present_jobKey)
      list.add(jobKey);

    boolean present_instanceId = true;
    list.add(present_instanceId);
    if (present_instanceId)
      list.add(instanceId);

    return list.hashCode();
  }

  @Override
  public int compareTo(InstanceKey other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetJobKey()).compareTo(other.isSetJobKey());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetJobKey()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.jobKey, other.jobKey);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetInstanceId()).compareTo(other.isSetInstanceId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetInstanceId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.instanceId, other.instanceId);
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
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("InstanceKey(");
    boolean first = true;

    sb.append("jobKey:");
    if (this.jobKey == null) {
      sb.append("null");
    } else {
      sb.append(this.jobKey);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("instanceId:");
    sb.append(this.instanceId);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
    if (jobKey != null) {
      jobKey.validate();
    }
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class InstanceKeyStandardSchemeFactory implements SchemeFactory {
    public InstanceKeyStandardScheme getScheme() {
      return new InstanceKeyStandardScheme();
    }
  }

  private static class InstanceKeyStandardScheme extends StandardScheme<InstanceKey> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, InstanceKey struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // JOB_KEY
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.jobKey = new JobKey();
              struct.jobKey.read(iprot);
              struct.setJobKeyIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // INSTANCE_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.instanceId = iprot.readI32();
              struct.setInstanceIdIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, InstanceKey struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.jobKey != null) {
        oprot.writeFieldBegin(JOB_KEY_FIELD_DESC);
        struct.jobKey.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(INSTANCE_ID_FIELD_DESC);
      oprot.writeI32(struct.instanceId);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class InstanceKeyTupleSchemeFactory implements SchemeFactory {
    public InstanceKeyTupleScheme getScheme() {
      return new InstanceKeyTupleScheme();
    }
  }

  private static class InstanceKeyTupleScheme extends TupleScheme<InstanceKey> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, InstanceKey struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetJobKey()) {
        optionals.set(0);
      }
      if (struct.isSetInstanceId()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetJobKey()) {
        struct.jobKey.write(oprot);
      }
      if (struct.isSetInstanceId()) {
        oprot.writeI32(struct.instanceId);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, InstanceKey struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        struct.jobKey = new JobKey();
        struct.jobKey.read(iprot);
        struct.setJobKeyIsSet(true);
      }
      if (incoming.get(1)) {
        struct.instanceId = iprot.readI32();
        struct.setInstanceIdIsSet(true);
      }
    }
  }

}

