package pictobrick.service;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

import pictobrick.model.Configuration;
import pictobrick.model.DataManagement;
import pictobrick.ui.MainWindow;
import pictobrick.ui.ProgressBarsAlgorithms;

/**
 * Provides appropriate for data processing.
 *
 * @author Adrian Schuetz
 */
public class DataProcessor {
    /** Show information in dialog. */
    public static final int SHOW_IN_DIALOG = 1;
    /** Show information in output files. */
    public static final int SHOW_IN_OUTPUT = 2;
    /** Show information in both dialog and output files. */
    public static final int SHOW_IN_BOTH = 3;
    /** Naive RGB quatization. */
    public static final int NAIVE_RGB = 1;
    /** Floyd/Steinberg quantization. */
    public static final int FLOYD_STEINBERG = 2;
    /** Vector error diffusion quantization. */
    public static final int VECTOR_ERROR_DIFFUSION = 3;
    /** Pattern dithering quantization. */
    public static final int PATTERN_DITHERING = 4;
    /** Solid regions quantization. */
    public static final int SOLID_REGIONS = 5;
    /** Slicing quantization.. */
    public static final int SLICING = 6;
    /** Naive LAB quantization. */
    public static final int NAIVE_LAB = 7;
    /** Element size optimization. */
    private static final int ELEMENT_SIZE_OPTIMIZATION = 1;
    /** Molding optimization. */
    private static final int MOLDING_OPTIMIZATION = 2;
    /** Costs optimization. */
    private static final int COSTS_OPTIMIZATION = 3;
    /** Stability optimization. */
    private static final int STABILITY_OPTIMIZATION = 4;
    /** Basic elements only. */
    private static final int BASIC_ELEMENTS_ONLY = 5;
    /** Text resource bundle. */
    private static ResourceBundle textbundle = ResourceBundle
            .getBundle("Resources.TextResource");
    /** Contains all program data and provides appropriate methods. */
    private final DataManagement dataManagement;

    /**
     * Returns data manager.
     *
     * @return data manager.
     */
    public DataManagement getDataManagement() {
        return dataManagement;
    }

    /** Main application window. */
    private final MainWindow mainWindow;

    /**
     * Returns main application window.
     *
     * @return main application window.
     */
    public MainWindow getMainWindow() {
        return mainWindow;
    }

    /** Output file generator. */
    private final OutputFileGenerator outputFiles;
    /** Calculator. */
    private final Calculator calculation;

    /**
     * Returns calculator.
     *
     * @return calculator.
     */
    public Calculator getCalculation() {
        return calculation;
    }

    /** Reference to DataProcessor instance. */
    private final DataProcessor dataProcessing;
    /** Dialog information. */
    private Vector<String> info1;
    /** Output file information. */
    private Vector<String> info2;
    /** Quantization info. */
    private Vector<Object> quantisationInfo;
    /** Tiling info. */
    private Vector<Object> tilingInfo;

    /**
     * Returns tiling info.
     *
     * @return tiling info.
     */
    public Vector<Object> getTilingInfo() {
        return tilingInfo;
    }

    /** Indicates whether to use 3D effect. */
    private boolean threeDEffect;
    /** Indicates whether to output statistics. */
    private boolean statisticOutput;

    /**
     * Indicates whether to output statistics.
     *
     * @return <code>true</code> if output statistics.
     */
    public boolean isStatisticOutput() {
        return statisticOutput;
    }

    /** Quantization algorithm. */
    private int quantisationAlgo;
    /** Tiling algorithm. */
    private int tilingAlgo;
    /** Naive RGB Quantizer. */
    private NaiveRgbQuantizer naiveQuantisation;
    /** Floyd/Steinberg Quantizer. */
    private FloydSteinbergQuantizer floydSteinberg;
    /** Naive LAB quantizer. */
    private NaiveLabQuantizer naiveQuantisationLab;
    /** Slicer. */
    private Slicer slicing;
    /** Solid regions quantizer. */
    private SolidRegionsQuantizer solidRegions;
    /** Pattern dithering quantizer. */
    private PatternDitheringQuantizer patternDithering;
    /** Vector error diffuser. */
    private VectorErrorDiffuser vectorErrorDiffusion;
    /** Element size optimizer. */
    private ElementSizeOptimizer elementSizeOptimisation;

    /**
     * Returns element size optimizer.
     *
     * @return element size optimizer.
     */
    public ElementSizeOptimizer getElementSizeOptimisation() {
        return elementSizeOptimisation;
    }

    /**
     * Sets element size optimizer.
     *
     * @param optimizer element size optimizer.
     */
    public void setElementSizeOptimisation(
            final ElementSizeOptimizer optimizer) {
        this.elementSizeOptimisation = optimizer;
    }

    /** Interpolation. */
    private int interpolation;

    /**
     * Constructor.
     *
     * @author Adrian Schuetz
     * @param appWindow
     */
    public DataProcessor(final MainWindow appWindow) {
        dataManagement = new DataManagement(this);
        this.mainWindow = appWindow;
        outputFiles = new OutputFileGenerator(this);
        calculation = new Calculator();
        dataProcessing = this;
        initInfo();
    }

    /**
     * Generate mosaic.
     *
     * @author Adrian Schuetz
     * @param mosaicWidth
     * @param mosaicHeight
     * @param quantisation
     * @param tiling
     * @param threeD
     * @param statistic
     */
    public void generateMosaic(final int mosaicWidth, final int mosaicHeight,
            final int quantisation, final int tiling, final boolean threeD,
            final boolean statistic) {
        initInfo();
        this.threeDEffect = threeD;
        this.statisticOutput = statistic;
        this.tilingAlgo = tiling;
        this.quantisationAlgo = quantisation;
        dataManagement.generateMosaic(mosaicWidth, mosaicHeight);
        // Vectors for dialog imput
        quantisationInfo = new Vector<>();
        tilingInfo = new Vector<>();
        // Call dialogs
        // -----------------------------------------------------------
        determineQuantizationAlgorithm();
        determineTilingAlgorithm();
        // Algorithms
        mainWindow.refreshProgressBarAlgorithm(0,
                ProgressBarsAlgorithms.QUANTIZATION);
        mainWindow.refreshProgressBarAlgorithm(0,
                ProgressBarsAlgorithms.TILING);
        mainWindow.refreshProgressBarAlgorithm(0,
                ProgressBarsAlgorithms.STATISTICS);
        mainWindow.refreshProgressBarAlgorithm(0, ProgressBarsAlgorithms.PAINT);
        mainWindow.setStatusProgressBarAlgorithm(statisticOutput);
        mainWindow.showProgressBarAlgorithm();
        // SwingWorker
        // "construct": all commands are startet in a new thread
        // "finished": all commands are queued to the gui thread
        // after finshing aforesaid (construct-)thread
        final SwingWorker worker = new SwingWorker() {
            public Object construct() {
                switch (quantisationAlgo) {
                case NAIVE_RGB:
                    naiveQuantisation = new NaiveRgbQuantizer(dataProcessing,
                            calculation);
                    naiveQuantisation.quantisation(
                            dataManagement.getImage(false),
                            dataManagement.getMosaicWidth(),
                            dataManagement.getMosaicHeight(),
                            dataManagement.getCurrentConfiguration(),
                            dataManagement.getMosaicInstance());
                    naiveQuantisation = null;
                    break;
                case FLOYD_STEINBERG:
                    floydSteinberg = new FloydSteinbergQuantizer(dataProcessing,
                            calculation, quantisationInfo);
                    floydSteinberg.quantisation(dataManagement.getImage(false),
                            dataManagement.getMosaicWidth(),
                            dataManagement.getMosaicHeight(),
                            dataManagement.getCurrentConfiguration(),
                            dataManagement.getMosaicInstance());
                    floydSteinberg = null;
                    break;
                case VECTOR_ERROR_DIFFUSION:
                    vectorErrorDiffusion = new VectorErrorDiffuser(
                            dataProcessing, calculation);
                    vectorErrorDiffusion.quantisation(
                            dataManagement.getImage(false),
                            dataManagement.getMosaicWidth(),
                            dataManagement.getMosaicHeight(),
                            dataManagement.getCurrentConfiguration(),
                            dataManagement.getMosaicInstance());
                    vectorErrorDiffusion = null;
                    break;
                case PATTERN_DITHERING:
                    patternDithering = new PatternDitheringQuantizer(
                            dataProcessing, calculation, quantisationInfo);
                    patternDithering.quantisation(
                            dataManagement.getImage(false),
                            dataManagement.getMosaicWidth(),
                            dataManagement.getMosaicHeight(),
                            dataManagement.getCurrentConfiguration(),
                            dataManagement.getMosaicInstance());
                    patternDithering = null;
                    break;
                case SOLID_REGIONS:
                    solidRegions = new SolidRegionsQuantizer(dataProcessing,
                            calculation);
                    solidRegions.quantisation(dataManagement.getImage(false),
                            dataManagement.getMosaicWidth(),
                            dataManagement.getMosaicHeight(),
                            dataManagement.getCurrentConfiguration(),
                            dataManagement.getMosaicInstance());
                    solidRegions = null;
                    break;
                case SLICING:
                    slicing = new Slicer(dataProcessing, calculation,
                            quantisationInfo);
                    slicing.quantisation(dataManagement.getImage(false),
                            dataManagement.getMosaicWidth(),
                            dataManagement.getMosaicHeight(),
                            dataManagement.getCurrentConfiguration(),
                            dataManagement.getMosaicInstance());
                    slicing = null;
                    break;
                case NAIVE_LAB:
                    naiveQuantisationLab = new NaiveLabQuantizer(dataProcessing,
                            calculation);
                    naiveQuantisationLab.quantisation(
                            dataManagement.getImage(false),
                            dataManagement.getMosaicWidth(),
                            dataManagement.getMosaicHeight(),
                            dataManagement.getCurrentConfiguration(),
                            dataManagement.getMosaicInstance());
                    naiveQuantisationLab = null;
                    break;
                default:
                    break;
                }
                return true;
            }

            public void finished() {
                dataProcessing.tileMosaic(tilingAlgo, threeDEffect);
            }
        };
        worker.start();
    }

    private void determineTilingAlgorithm() {
        switch (tilingAlgo) {
        case ELEMENT_SIZE_OPTIMIZATION:
            break;
        case MOLDING_OPTIMIZATION:
            // moldingOptimisation
            // Optimisation dialog only in combination with the 3 methods below
            if (quantisationAlgo == FLOYD_STEINBERG) {
                tilingInfo = mainWindow.dialogMoldingOptimisation(
                        FLOYD_STEINBERG,
                        textbundle.getString("algorithm_errorDiffusion"));
            } else if (quantisationAlgo == VECTOR_ERROR_DIFFUSION) {
                tilingInfo = mainWindow.dialogMoldingOptimisation(
                        VECTOR_ERROR_DIFFUSION,
                        textbundle.getString("algorithm_vectorErrorDiffusion"));
            } else if (quantisationAlgo == PATTERN_DITHERING) {
                tilingInfo = mainWindow.dialogMoldingOptimisation(
                        PATTERN_DITHERING,
                        textbundle.getString("algorithm_patternDithering"));
            } else {
                break;
            }
        case COSTS_OPTIMIZATION:
            break;
        case STABILITY_OPTIMIZATION:
            // Stability
            tilingInfo = mainWindow.dialogStabilityOptimisation();
            tilingInfo.insertElementAt(quantisationAlgo, 0);

            if (quantisationAlgo == 2) {
                tilingInfo.add(quantisationInfo.elementAt(0));
                tilingInfo.add(quantisationInfo.elementAt(1));
            } else if (quantisationAlgo == SLICING) {
                final var quantisationInfoEnum = quantisationInfo.elements();

                while (quantisationInfoEnum.hasMoreElements()) {
                    tilingInfo.add(quantisationInfoEnum.nextElement());
                }
            }

            break;
        default:
            break;
        }
    }

    private void determineQuantizationAlgorithm() {
        switch (quantisationAlgo) {
        case NAIVE_RGB:
            break;
        case FLOYD_STEINBERG:
            // FloydSteinberg
            quantisationInfo = mainWindow.dialogFloydSteinberg(
                    dataManagement.getCurrentConfiguration().getAllColors());
            break;
        case VECTOR_ERROR_DIFFUSION:
            break;
        case PATTERN_DITHERING:
            // Pattern dithering
            quantisationInfo = mainWindow.dialogPatternDithering();
            break;
        case SOLID_REGIONS:
            break;
        case SLICING:
            // Slicing
            quantisationInfo = mainWindow.dialogSlicingThreshold(
                    mainWindow.dialogSlicingColor(),
                    dataManagement.getCurrentConfiguration().getAllColors());
            break;
        case NAIVE_LAB:
            break;
        default:
            break;
        }
    }

    /**
     * Tiles the mosaic (own methode because of "threads").
     *
     * @author Adrian Schuetz
     * @param tiling (algorithm)
     * @param threeD <code>true</code> if using 3D effect.
     */
    public void tileMosaic(final int tiling, final boolean threeD) {
        this.threeDEffect = threeD;
        this.tilingAlgo = tiling;
        // SwingWorker
        // "construct": all commands are startet in a new thread
        // "finished": all commands are queued to the gui thread
        // after finshing aforesaid (construct-)thread
        final SwingWorker worker = new SwingWorker() {
            public Object construct() {
                final String outputDataProcessing1 = textbundle
                        .getString("output_dataProcessing_1");
                final String outputDataProcessing2 = textbundle
                        .getString("output_dataProcessing_2");
                final String outputDataProcessing3 = textbundle
                        .getString("output_dataProcessing_3");
                final String outputDataProcessing4 = textbundle
                        .getString("output_dataProcessing_4");
                final String outputDataProcessing5 = textbundle
                        .getString("output_dataProcessing_5");
                final String outputDataProcessing6 = textbundle
                        .getString("output_dataProcessing_6");
                final var processor = new TilingAlgorithmProcessor(
                        dataProcessing);

                switch (tilingAlgo) {
                case ELEMENT_SIZE_OPTIMIZATION:
                    processor.doElementSizeOptimization(outputDataProcessing1,
                            outputDataProcessing2, outputDataProcessing3,
                            outputDataProcessing4);
                    break;
                case MOLDING_OPTIMIZATION:
                    processor.doMoldingOptimization(outputDataProcessing1);
                    break;
                case COSTS_OPTIMIZATION:
                    processor.doCostsOptimization(outputDataProcessing1);
                    break;
                case STABILITY_OPTIMIZATION:
                    processor.doStabilityOptimization(outputDataProcessing5,
                            outputDataProcessing6);
                    break;
                case BASIC_ELEMENTS_ONLY:
                    processor.doBasicElementsOnly();
                    break;
                default:
                    break;
                }

                return true;
            }

            public void finished() {
                dataManagement.generateMosaicImage(threeDEffect);
            }
        };

        worker.start();
    }

    /**
     * Shows mosaic.
     *
     * @author Adrian Schuetz
     */
    public void showMosaic() {
        mainWindow.hideProgressBarAlgorithm();
        mainWindow.showMosaic();
    }

    /**
     * Retruns the tiling algorithm (important for statistic evaluation).
     *
     * @author Adrian Schuetz
     * @return Number of the algorithm
     */
    public int getTilingAlgorithm() {
        return this.tilingAlgo;
    }

    /**
     * Returns the selected interpolation method.
     *
     * @author Adrian Schuetz
     * @return 1=bicubic, 2=bilinear, 3=nearestneighbor
     */
    public int getInterpolation() {
        return this.interpolation;
    }

    /**
     * Sets the interpolation method.
     *
     * @author Adrian Schuetz
     * @param method 1=bicubic, 2=bilinear, 3=nearestneighbor
     */
    public void setInterpolation(final int method) {
        this.interpolation = method;
    }

    /**
     * Changes the display value.
     *
     * @author Adrian Schuetz
     * @param value
     * @param number 1:quantisation and 2:tiling
     */
    public void refreshProgressBarAlgorithm(final int value, final int number) {
        mainWindow.refreshProgressBarAlgorithm(value, number);
    }

    /**
     * Changes the display value.
     *
     * @author Adrian Schuetz
     * @param value
     * @param number (ID for progressBar)
     */
    public void refreshProgressBarOutputFiles(final int value,
            final int number) {
        mainWindow.refreshProgressBarOutputFiles(value, number);
    }

    /**
     * Animates the progressBar.
     *
     * @author Adrian Schuetz
     * @param on (/off)
     */
    public void animateGraficProgressBarOutputFiles(final boolean on) {
        mainWindow.animateGraficProgressBarOutputFiles(on);
    }

    /**
     * Returns an enumation with Information.
     *
     * @author Adrian Schuetz
     * @param displayLocation 1 = dialog; 2 = output file.
     * @return Enumeration
     */
    public Enumeration<String> getInfo(final int displayLocation) {
        Enumeration<String> infoEnum = null;

        if (displayLocation == 1) {
            infoEnum = info1.elements();
        } else if (displayLocation == 2) {
            infoEnum = info2.elements();
        }

        return infoEnum;
    }

    /**
     * Calls the appropriate method in dataManagement.
     *
     * @author Adrian Schuetz
     * @return mosaicHeight
     */
    public int getMosaicHeight() {
        return dataManagement.getMosaicHeight();
    }

    /**
     * Calls the appropriate method in dataManagement.
     *
     * @author Adrian Schuetz
     * @return mosaicWidth
     */
    public int getMosaicWidth() {
        return dataManagement.getMosaicWidth();
    }

    /**
     * Adds the information to the informationvector.
     *
     * @author Adrian Schuetz
     * @param text
     * @param number = 1: show in dialog 2: show in output documents 3: show in
     *               both (1 and 2)
     */
    public void setInfo(final String text, final int number) {
        if (number == SHOW_IN_DIALOG) {
            info1.addElement(text);
        } else if (number == SHOW_IN_OUTPUT) {
            info2.addElement(text);
        } else {
            info1.addElement(text);
            info2.addElement(text);
        }
    }

    /**
     * Initialize the information vector.
     *
     * @author Adrian Schuetz
     */
    public void initInfo() {
        info1 = new Vector<>();
        info2 = new Vector<>();
    }

    /**
     * Calls the appropriate method in dataManagement.
     *
     * @author Adrian Schuetz
     * @return Vector[][]
     */
    public List<List<Vector<String>>> getMosaic() {
        return dataManagement.getMosaic();
    }

    /**
     * Calls the appropriate method in dataManagement.
     *
     * @author Adrian Schuetz
     * @param image
     * @param fileName
     * @return true or false
     */
    public boolean generateImageOutput(final BufferedImage image,
            final String fileName) {
        return dataManagement.generateImageOutput(image, fileName);
    }

    /**
     * Calls the appropriate method in dataManagement.
     *
     * @author Adrian Schuetz
     * @param color
     * @param colorName
     * @return true or false
     */
    public boolean generateColorOutput(final Color color,
            final String colorName) {
        return dataManagement.generateColorOutput(color, colorName);
    }

    /**
     * Calls the appropriate method in dataManagement.
     *
     * @author Adrian Schuetz
     * @param content
     * @param fileName
     * @param subFolder
     * @return true or false
     */
    public boolean generateUTFOutput(final String content,
            final String fileName, final boolean subFolder) {
        return dataManagement.generateUTFOutput(content, fileName, subFolder);
    }

    /**
     * Generates the output documents.
     *
     * @author Adrian Schuetz
     * @param image         true, if document should be generated
     * @param configuration true, if document should be generated
     * @param material      true, if document should be generated
     * @param instruction   true, if document should be generated
     * @param xml           true, if document should be generated
     * @param infos
     * @return message (error/sucess)
     */
    public String generateDocuments(final boolean image,
            final boolean configuration, final boolean material,
            final boolean instruction, final boolean xml,
            final Enumeration<String> infos) {
        outputFiles.setProject(dataManagement.generateFolderOutput());
        return outputFiles.generateDocuments(image, configuration, material,
                instruction, xml, infos);
    }

    /**
     * Calls the appropriate method in dataManagement.
     *
     * @author Adrian Schuetz
     */
    public void imageReset() {
        dataManagement.imageReset();
    }

    /**
     * Calls the appropriate method in dataManagement.
     *
     * @author Adrian Schuetz
     * @param file
     * @return true or false
     */
    public boolean imageLoad(final File file) {
        return dataManagement.imageLoad(file);
    }

    /**
     * Calls the appropriate method in dataManagement.
     *
     * @author Adrian Schuetz
     * @param cutout
     */
    public void replaceImageByCutout(final Rectangle cutout) {
        dataManagement.replaceImageByCutout(cutout);
    }

    /**
     * Calls the appropriate method in dataManagement (scaling with
     * sliderValue).
     *
     * @author Adrian Schuetz
     * @param mosaic
     * @param sliderValue
     * @return BufferedImage
     */
    public BufferedImage getScaledImage(final boolean mosaic,
            final int sliderValue) {
        return dataManagement.getScaledImage(mosaic, sliderValue);
    }

    /**
     * Calls the appropriate method in dataManagement.
     *
     * @author Adrian Schuetz
     * @param mosaic
     * @return BufferedImage
     */
    public BufferedImage getImage(final boolean mosaic) {
        return dataManagement.getImage(mosaic);
    }

    /**
     * Calls the appropriate method in dataManagement.
     *
     * @author Adrian Schuetz
     * @return true or false
     */
    public boolean isImage() {
        return dataManagement.isImage();
    }

    /**
     * Calls the appropriate method in dataManagement.
     *
     * @author Adrian Schuetz
     * @return true or false
     */
    public boolean isConfiguration() {
        return dataManagement.isConfiguration();
    }

    /**
     * Calls the appropriate method in dataManagement.
     *
     * @author Adrian Schuetz
     * @param file
     * @return configuration
     */
    public Configuration getSystemConfiguration(final int file) {
        return dataManagement.getSystemConfiguration(file);
    }

    /**
     * Calls the appropriate method in dataManagement.
     *
     * @author Adrian Schuetz
     * @param configuration
     */
    public void setCurrentConfiguration(final Configuration configuration) {
        dataManagement.setCurrentConfiguration(configuration);
    }

    /**
     * Calls the appropriate method in dataManagement.
     *
     * @author Adrian Schuetz
     * @return configuration
     */
    public Configuration getCurrentConfiguration() {
        return dataManagement.getCurrentConfiguration();
    }

    /**
     * Calls the appropriate method in dataManagement.
     *
     * @author Adrian Schuetz
     * @param configuration
     */
    public void configurationSave(final Configuration configuration)
            throws IOException {
        dataManagement.configurationSave(configuration);
    }

    /**
     * Calls the appropriate method in dataManagement.
     *
     * @author Adrian Schuetz
     * @param configuration
     * @return the configuration just loaded.
     * @exception IOException
     */
    public Configuration configurationLoad(final String configuration)
            throws IOException {
        return dataManagement.configurationLoad(configuration);
    }

    /**
     * Calls the appropriate method in dataManagement.
     *
     * @author Adrian Schuetz
     * @param mosaic
     * @param width
     * @param height
     */
    public void computeScaleFactor(final boolean mosaic, final double width,
            final double height) {
        dataManagement.computeScaleFactor(mosaic, width, height);
    }

    /**
     * Calls the appropriate method in dataManagement.
     *
     * @author Adrian Schuetz
     * @param file
     */
    public void setWorkingDirectory(final File file) {
        dataManagement.setWorkingDirectory(file);
    }

    /**
     * Calls the appropriate method in dataManagement.
     *
     * @author Adrian Schuetz
     * @return file
     */
    public File getWorkingDirectory() {
        return dataManagement.getWorkingDirectory();
    }

    /**
     * Calls the appropriate method in dataManagement.
     *
     * @author Adrian Schuetz
     * @return vector with possible configurations
     */
    public Vector<String> getConfiguration() {
        return dataManagement.getConfiguration();
    }

    /**
     * Calls the appropriate method in dataManagement.
     *
     * @author Adrian Schuetz
     * @return dimension
     */
    public Dimension getConfigurationDimension() {
        return dataManagement.getConfigurationDimension();
    }

    /**
     * Calls the appropriate method in dataManagement.
     *
     * @author Adrian Schuetz
     * @param error
     */
    public void errorDialog(final String error) {
        mainWindow.errorDialog(error);
    }

    /**
     * Calls the appropriate method in dataManagement.
     *
     * @author Adrian Schuetz
     * @param workingDirectory
     * @return true or false
     */
    public boolean saveWorkingDirectory(final File workingDirectory) {
        return dataManagement.saveWorkingDirectory(workingDirectory);
    }

    /**
     * Calls the appropriate method in dataManagement.
     *
     * @author Adrian Schuetz
     * @return working directory
     */
    public File loadWorkingDirectory() {
        return dataManagement.loadWorkingDirectory();
    }
}
