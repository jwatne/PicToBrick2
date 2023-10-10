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
    /** New configuration selection. */
    public static final int NEW_CONFIGURATION = 1;
    /** Derivitve configuration selection. */
    public static final int DERIVITIVE_CONFIGURATION = 2;
    /** Load existing configuration selection. */
    public static final int LOAD_EXISTING_CONFIGURATION = 3;
    /** Offset substracted from substring value for string matching. */
    public static final int SUBSTRING_OFFSET = 4;
    /** Minimum configuration file value. */
    private static final int MIN_FILE_VALUE = 3;
    /** The main window of the application. */
    private final MainWindow mainWindow;
    /** The data processor. */
    private final DataProcessor dataProcessing;

    /**
     * Constructs a configuration loader for the main window.
     *
     * @param window the main window of the application.
     */
    public ConfigurationLoader(final MainWindow window) {
        this.mainWindow = window;
        dataProcessing = window.getDataProcessing();
    }

    /**
     * Loads a configuration.
     *
     * @author Tobias Reichling
     */
    public void configurationLoad() {
        final Vector<String> configurationVector = dataProcessing
                .getConfiguration();
        var configurationLoadingDialog = new ConfigurationLoadingDialog(
                mainWindow, configurationVector);

        if (!configurationLoadingDialog.isCanceled()) {
            switch (configurationLoadingDialog.getSelection()) {
            // new configuration
            case NEW_CONFIGURATION:
                loadNewConfiguration();
                break;
            // derivate configuration
            case DERIVITIVE_CONFIGURATION:
                loadDerivitiveConfiguration(configurationVector,
                        configurationLoadingDialog);
                break;
            // load existing configuration
            case LOAD_EXISTING_CONFIGURATION:
                loadExistingConfiguration(configurationVector,
                        configurationLoadingDialog);
            default:
                break;
            }
        }

        configurationLoadingDialog = null;
    }

    private void loadExistingConfiguration(
            final Vector<String> configurationVector,
            final ConfigurationLoadingDialog configurationLoadingDialog) {
        final String existingFile = (String) configurationVector
                .elementAt(configurationLoadingDialog.getFile());

        if (configurationLoadingDialog.getFile() < MIN_FILE_VALUE) {
            dataProcessing.setCurrentConfiguration(
                    dataProcessing.getSystemConfiguration(
                            configurationLoadingDialog.getFile()));
            mainWindow.getGuiPanelOptions1()
                    .showConfigurationInfo(existingFile);
            mainWindow.showInfo(
                    MainWindow.getTextBundle().getString("output_mainWindow_13")
                            + " " + existingFile + " "
                            + MainWindow.getTextBundle()
                                    .getString("output_mainWindow_18")
                            + ".");
        } else {
            try {
                dataProcessing.setCurrentConfiguration(
                        dataProcessing.configurationLoad(existingFile));
                mainWindow.getGuiPanelOptions1()
                        .showConfigurationInfo(existingFile);
                mainWindow.showInfo(MainWindow.getTextBundle()
                        .getString("output_mainWindow_13") + " " + existingFile
                        + " " + MainWindow.getTextBundle()
                                .getString("output_mainWindow_18")
                        + ".");
            } catch (final IOException configurationLoadIO) {
                mainWindow.errorDialog(MainWindow.getTextBundle().getString(
                        "output_mainWindow_19") + ": " + configurationLoadIO);
            }
        }
    }

    private void loadDerivitiveConfiguration(
            final Vector<String> configurationVector,
            final ConfigurationLoadingDialog configurationLoadingDialog) {
        final String file = (String) configurationVector
                .elementAt(configurationLoadingDialog.getFile());
        Configuration configurationOld = new Configuration();

        if (configurationLoadingDialog.getFile() < MIN_FILE_VALUE) {
            configurationOld = dataProcessing.getSystemConfiguration(
                    configurationLoadingDialog.getFile());
        } else {
            try {
                configurationOld = dataProcessing.configurationLoad(file);
            } catch (final IOException loadIO) {
                mainWindow.errorDialog(MainWindow.getTextBundle()
                        .getString("output_mainWindow_16") + ": " + loadIO);
            }
        }

        var configDialog = new ConfigurationDerivationDialog(mainWindow, null,
                configurationOld);
        boolean cancel = false;

        while (!(cancel)) {
            if (!configDialog.isCanceled()) {
                boolean nameAvailable = false;
                String name = "";

                for (final Enumeration<String> results2 = dataProcessing
                        .getConfiguration().elements(); results2
                                .hasMoreElements();) {
                    name = ((String) results2.nextElement());

                    if ((name.substring(0, name.length() - SUBSTRING_OFFSET))
                            .equals(configDialog.getConfiguration()
                                    .getName())) {
                        nameAvailable = true;
                    }
                }

                if (nameAvailable) {
                    mainWindow.errorDialog(MainWindow.getTextBundle()
                            .getString("output_mainWindow_17"));
                    configDialog.showDialog();
                } else {
                    try {
                        dataProcessing.configurationSave(
                                configDialog.getConfiguration());
                        dataProcessing.setCurrentConfiguration(
                                configDialog.getConfiguration());
                        mainWindow.getGuiPanelOptions1().showConfigurationInfo(
                                configDialog.getConfiguration().getName()
                                        + ".cfg");
                        mainWindow.showInfo(MainWindow.getTextBundle()
                                .getString("output_mainWindow_13") + " "
                                + configDialog.getConfiguration().getName()
                                + ".cfg  " + MainWindow.getTextBundle()
                                        .getString("output_mainWindow_14")
                                + ".");
                        cancel = true;
                        configDialog = null;
                    } catch (final IOException speicherIO) {
                        mainWindow.errorDialog(MainWindow.getTextBundle()
                                .getString("output_mainWindow_15") + ": "
                                + speicherIO);
                        configDialog.showDialog();
                    }
                }
            } else {
                cancel = true;
                configDialog = null;
            }
        }
    }

    private void loadNewConfiguration() {
        var configurationNewDialog = new ConfigurationNewDialog(mainWindow);

        if (!configurationNewDialog.isCanceled()) {
            final Enumeration<Number> results = configurationNewDialog
                    .getValues().elements();
            final Configuration configurationNew = new Configuration(
                    configurationNewDialog.getName(),
                    new BasisElement(configurationNewDialog.getBasisName(),
                            (Integer) results.nextElement(),
                            (Integer) results.nextElement(),
                            (Double) results.nextElement()),
                    (Integer) results.nextElement(),
                    (Integer) results.nextElement(),
                    configurationNewDialog.getMaterial());
            var configDialog = new ConfigurationDerivationDialog(mainWindow,
                    configurationNew, null);
            boolean cancel = false;

            while (!(cancel)) {
                if (!configDialog.isCanceled()) {
                    try {
                        dataProcessing.configurationSave(
                                configDialog.getConfiguration());
                        dataProcessing.setCurrentConfiguration(
                                configDialog.getConfiguration());
                        mainWindow.getGuiPanelOptions1().showConfigurationInfo(
                                configDialog.getConfiguration().getName()
                                        + ".cfg");
                        mainWindow.showInfo(MainWindow.getTextBundle()
                                .getString("output_mainWindow_13") + " "
                                + configDialog.getConfiguration().getName()
                                + ".cfg " + MainWindow.getTextBundle()
                                        .getString("output_mainWindow_14")
                                + ".");
                        cancel = true;
                        configDialog = null;
                    } catch (final IOException saveIO) {
                        mainWindow.errorDialog(MainWindow.getTextBundle()
                                .getString("output_mainWindow_15") + ": "
                                + saveIO);
                        configDialog.showDialog();
                    }
                }

                cancel = true;
                configDialog = null;
            }
        }

        configurationNewDialog = null;
    }
}
