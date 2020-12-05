/**
 * Copyright (C) 2008 Happy Fish / YuQing
 * <p>
 * FastDFS Java Client may be copied only under the terms of the GNU Lesser
 * General Public License (LGPL).
 * Please visit the FastDFS Home Page https://github.com/happyfish100/fastdfs for more detail.
 */

package org.common.fastdfs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Tracker Server Info
 *
 * @author Happy Fish / YuQing
 * @version Version 1.11
 */
public class TrackerServer {
  protected Socket sock;
  protected InetSocketAddress inetSockAddr;

  /**
   * Constructor
   *
   * @param sock         Socket of server
   * @param inetSockAddr the server info
   */
  public TrackerServer(Socket sock, InetSocketAddress inetSockAddr) {
    this.sock = sock;
    this.inetSockAddr = inetSockAddr;
  }

  /**
   * get the connected socket
   *
   * @return the socket
   */
  public Socket getSocket() throws IOException {
    if (this.sock == null) {
      this.sock = ClientGlobal.getSocket(this.inetSockAddr);
    }

    return this.sock;
  }

  /**
   * get the server info
   *
   * @return the server info
   */
  public InetSocketAddress getInetSocketAddress() {
    return this.inetSockAddr;
  }

  public OutputStream getOutputStream() throws IOException {
    return this.sock.getOutputStream();
  }

  public InputStream getInputStream() throws IOException {
    return this.sock.getInputStream();
  }

  public void close() throws IOException {
    if (this.sock != null) {
      try {
        ProtoCommon.closeSocket(this.sock);
      } finally {
        this.sock = null;
      }
    }
  }

  protected void finalize() throws Throwable {
    this.close();
  }

  public boolean isConnected(){
    boolean isConnected = false;
    if (sock != null) {
      if (sock.isConnected()) {
        isConnected = true;
      }
    }
    return isConnected;
  }

  public boolean isAvaliable() {
    if (isConnected()) {
      if (sock.getPort() == 0) {
        return false;
      }
      if (sock.getInetAddress() == null) {
        return false;
      }
      if (sock.getRemoteSocketAddress() == null) {
        return false;
      }
      if (sock.isInputShutdown()) {
        return false;
      }
      if (sock.isOutputShutdown()) {
        return false;
      }
      return true;
    }
    return false;
  }
}
