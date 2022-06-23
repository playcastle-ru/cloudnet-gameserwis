package pl.memexurer.gaming.chat;

import de.dytanic.cloudnet.common.io.FileUtils;
import de.dytanic.cloudnet.driver.event.EventListener;
import de.dytanic.cloudnet.driver.service.ServiceEnvironmentType;
import de.dytanic.cloudnet.driver.util.DefaultModuleHelper;
import de.dytanic.cloudnet.event.service.CloudServicePreStartEvent;
import de.dytanic.cloudnet.ext.bridge.node.CloudNetBridgeModule;
import java.nio.file.Path;
import java.util.Arrays;

public class PluginIncludeListener {

  @EventListener
  public void handle(CloudServicePreStartEvent event) {
    if(event.getCloudService().getServiceId().getEnvironment() != ServiceEnvironmentType.VELOCITY)
      return;

    boolean installPlugin = CloudNetBridgeModule.getInstance().getBridgeConfiguration().getExcludedGroups()
        .stream()
        .noneMatch(excludedGroup -> Arrays.asList(event.getCloudService().getServiceConfiguration().getGroups())
            .contains(excludedGroup));

    Path pluginsFolder = event.getCloudService().getDirectoryPath().resolve("plugins");
    FileUtils.createDirectoryReported(pluginsFolder);

    Path targetFile = pluginsFolder.resolve("cloudnet-gamingchat.jar");
    FileUtils.deleteFileReported(targetFile);

    if (installPlugin && DefaultModuleHelper
        .copyCurrentModuleInstanceFromClass(PluginIncludeListener.class, targetFile)) {
      DefaultModuleHelper.copyPluginConfigurationFileForEnvironment(
          PluginIncludeListener.class,
          event.getCloudService().getServiceConfiguration().getProcessConfig().getEnvironment(),
          targetFile
      );
    }
  }
}
