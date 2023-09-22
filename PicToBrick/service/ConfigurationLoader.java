package pictobrick.service;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import pictobrick.model.BasisElement;
import pictobrick.model.Configuration;
import pictobrick.ui.ConfigurationDerivationDialog;
import pictobrick.ui.ConfigurationLoadingDialog;
import pictobrick.ui.ConfigurationNewDialog;
import pictobrick.ui.MainWindow;

/**
 * Configuration loading class. Code moved from {@link pictobrick.ui.MainWindow}
 * by John Watne 09/2023.
 */
public class ConfigurationLoader {
    private final MainWindow mainWindow;
    private final DataProcessor dataProcessing;

    /**
     * Constructs a configuration loader for the main window.
     *
     * @param mainWindow the main window of the application.
     */
    public ConfigurationLoader(final MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        dataProcessing = mainWindow.getDataProcessing();
    }

    /**
     * method: configurationLoad description: loads a configuration
     *
     * @author Tobias Reichling
     */
    public void configurationLoad() {
        final Vector<String> configurationVector = dataProcessing
                .getConfiguration();
        ConfigurationLoadingDialog configurationLoadingDialog = new ConfigurationLoadingDialog(
                mainWindow, configurationVector);

        if (!configurationLoadingDialog.isCanceled()) {
            switch (configurationLoadingDialog.getSelection()) {
            // new configuration
            case 1:
                ConfigurationNewDialog configurationNewDialog = new ConfigurationNewDialog(
                        mainWindow);

                if (!configurationNewDialog.isCanceled()) {
                    final Enumeration<Number> results = configurationNewDialog
                            .getValues().elements();
                    final Configuration configurationNew = new Configuration(
                            configurationNewDialog.getName(),
                            new BasisElement(
                                    configurationNewDialog.getBasisName(),
                                    (Integer) results.nextElement(),
                                    (Integer) results.nextElement(),
                                    (Double) results.nextElement()),
                            (Integer) results.nextElement(),
                            (Integer) results.nextElement(),
                            configurationNewDialog.getMaterial());
                    ConfigurationDerivationDialog configurationDerivationDialog = new ConfigurationDerivationDialog(
                            mainWindow, configurationNew, null);
                    boolean cancel = false;

                    while (!(cancel)) {
                        if (!configurationDerivationDialog.isCanceled()) {
                            try {
                                dataProcessing.configurationSave(
                                        configurationDerivationDialog
                                                .getConfiguration());
                                dataProcessing.setCurrentConfiguration(
                                        configurationDerivationDialog
                                                .getConfiguration());
                                mainWindow.getGuiPanelOptions1()
                                        .showConfigurationInfo(
                                                configurationDerivationDialog
                                                        .getConfiguration()
                                                        .getName() + ".cfg");
                                mainWindow.showInfo(MainWindow.getTextBundle()
                                        .getString("output_mainWindow_13")
                                        + " "
                                        + configurationDerivationDialog
                                                .getConfiguration().getName()
                                        + ".cfg "
                                        + MainWindow.getTextBundle().getString(
                                                "output_mainWindow_14")
                                        + ".");
                                cancel = true;
                                configurationDerivationDialog = null;
                            } catch (final IOException saveIO) {
                                mainWindow.errorDialog(
                                        MainWindow.getTextBundle().getString(
                                                "output_mainWindow_15") + ": "
                                                + saveIO);
                                configurationDerivationDialog.showDialog();
                            }
                        }

                        cancel = true;
                        configurationDerivationDialog = null;
                    }
                }
                configurationNewDialog = null;
                break;
            // derivate configuration
            case 2:
                final String file = (String) configurationVector
                        .elementAt(configurationLoadingDialog.getFile());
                Configuration configurationOld = new Configuration();

                if (configurationLoadingDialog.getFile() < 3) {
                    configurationOld = dataProcessing.getSystemConfiguration(
                            configurationLoadingDialog.getFile());
                } else {
                    try {
                        configurationOld = dataProcessing
                                .configurationLoad(file);
                    } catch (final IOException loadIO) {
                        mainWindow.errorDialog(MainWindow.getTextBundle()
                                .getString("output_mainWindow_16") + ": "
                                + loadIO);
                    }
                }

                ConfigurationDerivationDialog configurationDerivationDialog = new ConfigurationDerivationDialog(
                        mainWindow, null, configurationOld);
                boolean cancel = false;

                while (!(cancel)) {
                    if (!configurationDerivationDialog.isCanceled()) {
                        boolean nameAvailable = false;
                        String name = "";

                        for (final Enumeration<String> results2 = dataProcessing
                                .getConfiguration().elements(); results2
                                        .hasMoreElements();) {
                            name = ((String) results2.nextElement());

                            if ((name.substring(0, name.length() - 4))
                                    .equals(configurationDerivationDialog
                                            .getConfiguration().getName())) {
                                nameAvailable = true;
                            }
                        }

                        if (nameAvailable) {
                            mainWindow.errorDialog(MainWindow.getTextBundle()
                                    .getString("output_mainWindow_17"));
                            configurationDerivationDialog.showDialog();
                        } else {
                            try {
                                dataProcessing.configurationSave(
                                        configurationDerivationDialog
                                                .getConfiguration());
                                dataProcessing.setCurrentConfiguration(
                                        configurationDerivationDialog
                                                .getConfiguration());
                                mainWindow.getGuiPanelOptions1()
                                        .showConfigurationInfo(
                                                configurationDerivationDialog
                                                        .getConfiguration()
                                                        .getName() + ".cfg");
                                mainWindow.showInfo(MainWindow.getTextBundle()
                                        .getString("output_mainWindow_13")
                                        + " "
                                        + configurationDerivationDialog
                                                .getConfiguration().getName()
                                        + ".cfg  "
                                        + MainWindow.getTextBundle().getString(
                                                "output_mainWindow_14")
                                        + ".");
                                cancel = true;
                                configurationDerivationDialog = null;
                            } catch (final IOException speicherIO) {
                                mainWindow.errorDialog(
                                        MainWindow.getTextBundle().getString(
                                                "output_mainWindow_15") + ": "
                                                + speicherIO);
                                configurationDerivationDialog.showDialog();
                            }
                        }
                    } else {
                        cancel = true;
                        configurationDerivationDialog = null;
                    }
                }

                break;
            // load existing configuration
            case 3:
                final String existingFile = (String) configurationVector
                        .elementAt(configurationLoadingDialog.getFile());

                if (configurationLoadingDialog.getFile() < 3) {
                    dataProcessing.setCurrentConfiguration(
                            dataProcessing.getSystemConfiguration(
                                    configurationLoadingDialog.getFile()));
                    mainWindow.getGuiPanelOptions1()
                            .showConfigurationInfo(existingFile);
                    mainWindow.showInfo(MainWindow.getTextBundle()
                            .getString("output_mainWindow_13") + " "
                            + existingFile + " " + MainWindow.getTextBundle()
                                    .getString("output_mainWindow_18")
                            + ".");
                } else {
                    try {
                        dataProcessing.setCurrentConfiguration(
                                dataProcessing.configurationLoad(existingFile));
                        mainWindow.getGuiPanelOptions1()
                                .showConfigurationInfo(existingFile);
                        mainWindow.showInfo(MainWindow.getTextBundle()
                                .getString("output_mainWindow_13") + " "
                                + existingFile + " "
                                + MainWindow.getTextBundle().getString(
                                        "output_mainWindow_18")
                                + ".");
                    } catch (final IOException configurationLoadIO) {
                        mainWindow.errorDialog(MainWindow.getTextBundle()
                                .getString("output_mainWindow_19") + ": "
                                + configurationLoadIO);
                    }
                }
            default:
                break;
            }
        }

        configurationLoadingDialog = null;
    }
}
