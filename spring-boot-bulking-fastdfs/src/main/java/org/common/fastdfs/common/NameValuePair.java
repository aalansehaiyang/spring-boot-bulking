/*
* Copyright (C) 2008 Happy Fish / YuQing
*
* FastDFS Java Client may be copied only under the terms of the GNU Lesser
* General Public License (LGPL).
* Please visit the FastDFS Home Page https://github.com/happyfish100/fastdfs for more detail.
*/

package org.common.fastdfs.common;

/**
 * name(key) and value pair model
 *
 * @author Happy Fish / YuQing
 * @version Version 1.0
 */
public class NameValuePair {
  protected String name;
  protected String value;

  public NameValuePair() {
  }

  public NameValuePair(String name) {
    this.name = name;
  }

  public NameValuePair(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return this.value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
