/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package Enet;

public class workit_t {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected workit_t(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(workit_t obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        enetJNI.delete_workit_t(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setBuff(String value) {
    enetJNI.workit_t_buff_set(swigCPtr, this, value);
  }

  public String getBuff() {
    return enetJNI.workit_t_buff_get(swigCPtr, this);
  }

  public void setLen(int value) {
    enetJNI.workit_t_len_set(swigCPtr, this, value);
  }

  public int getLen() {
    return enetJNI.workit_t_len_get(swigCPtr, this);
  }

  public workit_t() {
    this(enetJNI.new_workit_t(), true);
  }

}
