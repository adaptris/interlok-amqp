package com.adaptris.core.amqp.qpid;

import static com.adaptris.interlok.junit.scaffolding.util.PortManager.release;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.store.memory.MemoryPersistenceAdapter;
import org.apache.commons.io.FileUtils;

import com.adaptris.interlok.junit.scaffolding.util.PortManager;
import com.adaptris.util.SafeGuidGenerator;

public class EmbeddedAMQP {

  private static final String AMQP_URL_PREFIX = "amqp://0.0.0.0:";
  private static final String OPENWIRE_URL_PREFIX = "tcp://localhost:";
  private BrokerService broker = null;
  private String brokerName;
  private File brokerDataDir;
  private Integer amqpPort;
  private Integer openwirePort;
  private String amqpConnectorURI;
  private String openwireConnectorURI;
  private static SafeGuidGenerator nameGenerator = new SafeGuidGenerator();

  public EmbeddedAMQP() throws Exception {
    brokerName = nameGenerator.create(this);
    openwirePort = PortManager.nextUnusedPort(61616);
    amqpPort = PortManager.nextUnusedPort(5672);
    openwireConnectorURI = OPENWIRE_URL_PREFIX + openwirePort + "?maximumConnections=1000&wireFormat.maxInactivityDuration=0";
    amqpConnectorURI = AMQP_URL_PREFIX + amqpPort + "?maximumConnections=1000&wireFormat.maxInactivityDuration=0";
  }

  public String getName() {
    return brokerName;
  }

  public void start() throws Exception {
    brokerDataDir = createTempFile(true);
    broker = createBroker();
    broker.start();
    while (!broker.isStarted()) {
      Thread.sleep(100);
    }
  }

  private BrokerService createBroker() throws Exception {
    BrokerService br = new BrokerService();
    br.setBrokerName(brokerName);
    br.addConnector(new URI(amqpConnectorURI));
    br.addConnector(new URI(openwireConnectorURI));
    br.setUseJmx(false);
    br.setDeleteAllMessagesOnStartup(true);
    br.setDataDirectoryFile(brokerDataDir);
    br.setPersistent(false);
    br.setPersistenceAdapter(new MemoryPersistenceAdapter());
    br.getSystemUsage().getMemoryUsage().setLimit(1024L * 1024 * 20);
    br.getSystemUsage().getTempUsage().setLimit(1024L * 1024 * 20);
    br.getSystemUsage().getStoreUsage().setLimit(1024L * 1024 * 20);
    br.getSystemUsage().getJobSchedulerUsage().setLimit(1024L * 1024 * 20);
    return br;
  }

  private File createTempFile(boolean isDir) throws IOException {
    File result = File.createTempFile("AMQ-", "");
    result.delete();
    if (isDir) {
      result.mkdirs();
    }
    return result;
  }

  public void destroy() throws Exception {
    new Thread(() -> {
      try {
        stop();
        release(amqpPort);
        release(openwirePort);
      } catch (Exception e) {

      }
    }).start();

  }

  public void stop() throws Exception {
    if (broker != null) {
      broker.stop();
      broker.waitUntilStopped();
      FileUtils.deleteDirectory(brokerDataDir);
    }
  }

  public String getBrokerUrl() {
    return "amqp://localhost:" + amqpPort + "?wireFormat.maxInactivityDuration=0";
  }

  public String getLegacyAMQPUrl() {
    String clientId = nameGenerator.safeUUID();
    return "amqp://" + clientId + "/?brokerlist='tcp://localhost:" + amqpPort + "'";
  }
}
