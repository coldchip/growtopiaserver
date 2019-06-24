/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package Enet;

public class ENetListNode {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected ENetListNode(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(ENetListNode obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        enetJNI.delete_ENetListNode(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setNext(ENetListNode value) {
    enetJNI.ENetListNode_next_set(swigCPtr, this, ENetListNode.getCPtr(value), value);
  }

  public ENetListNode getNext() {
    long cPtr = enetJNI.ENetListNode_next_get(swigCPtr, this);
    return (cPtr == 0) ? null : new ENetListNode(cPtr, false);
  }

  public void setPrevious(ENetListNode value) {
    enetJNI.ENetListNode_previous_set(swigCPtr, this, ENetListNode.getCPtr(value), value);
  }

  public ENetListNode getPrevious() {
    long cPtr = enetJNI.ENetListNode_previous_get(swigCPtr, this);
    return (cPtr == 0) ? null : new ENetListNode(cPtr, false);
  }

  public ENetListNode() {
    this(enetJNI.new_ENetListNode(), true);
  }

}
