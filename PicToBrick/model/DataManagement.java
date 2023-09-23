package pictobrick.model;

import pictobrick.service.DataProcessor;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;

/**
 * Contains all program data and provides appropriate methods.
 *
 * @author Adrian Schuetz
 */
public class DataManagement {
    /** Material index for Ministeck configuration. */
    private static final int MINISTECK_MATERIAL_INDEX = 3;
    /** Basis width for Ministeck configuration. */
    private static final double MINISTECK_BASIS_WIDTH = 4.16666;
    /** Stability for LEGO side view configuration. */
    private static final int LEGO_SIDE_STABILITY = 7;
    /** Basis width for LEGO side view configuration. */
    private static final int LEGO_SIDE_BASIS_WIDTH = 5;
    /** Basis width in Millimeters for the two LEGO view configurations. */
    private static final double LEGO_BASIS_WIDTH_MM = 8.0;
    /** Cost for the two LEGO view configurations. */
    private static final int LEGO_COSTS = 4;
    /** Text resource bundle. */
    private static ResourceBundle textbundle = ResourceBundle
            .getBundle("Resources.TextResource");
    /** Original image. */
    private Picture originalImage = null;
    /** Mosaic image. */
    private Picture mosaicImage = null;
    /** Current configuration. */
    private Configuration current;
    /** LEGO top view configuration. */
    private Configuration legoTop;
    /** LEGO side view configuration. */
    private Configuration legoSide;
    /** Ministeck configuration. */
    private Configuration ministeck;
    /** Working directory. */
    private File workingDirectory = null;
    /** Project folder. */
    private File projectFolder = null;
    /** Project name. */
    private String projectName = "";
    /** Configurations. */
    private Vector<String> configurations;
    /** Mosaic. */
    private Mosaic mosaic;
    /** Data processor. */
    private DataProcessor dataProcessing;

    /**
     * Constructor.
     *
     * @author Adrian Schuetz
     */
    public DataManagement() {
    }

    /**
     * Constructor.
     *
     * @author Adrian Schuetz
     * @param processor
     */
    public DataManagement(final DataProcessor processor) {
        this.dataProcessing = processor;
        configurations = new Vector<>();
        configurations.add("pictobrick_Lego_"
                + textbundle.getString("output_dataManagement_1") + ".cfg");
        configurations.add("pictobrick_Lego_"
                + textbundle.getString("output_dataManagement_2") + ".cfg");
        configurations.add("pictobrick_Ministeck.cfg");
        this.legoTop = new Configuration(
                "pictobrick_Lego_"
                        + textbundle.getString("output_dataManagement_1"),
                new BasisElement("Plate_1x1_(3024)", 1, 1, LEGO_BASIS_WIDTH_MM),
                1, LEGO_COSTS, 1);
        this.legoSide = new Configuration(
                "pictobrick_Lego_"
                        + textbundle.getString("output_dataManagement_2"),
                new BasisElement("Plate_1x1_(3024)", LEGO_SIDE_BASIS_WIDTH, 2,
                        LEGO_BASIS_WIDTH_MM),
                LEGO_SIDE_STABILITY, LEGO_COSTS, 2);
        this.ministeck = new Configuration("pictobrick_Ministeck",
                new BasisElement("Ministeck_1x1", 1, 1, MINISTECK_BASIS_WIDTH),
                1, 1, MINISTECK_MATERIAL_INDEX);
        final ConfigurationIntializer initializer = new ConfigurationIntializer(
                legoTop, legoSide, ministeck);
        legoTop = initializer.initConfigurationLegoTop();
        legoSide = initializer.initConfigurationLegoSide();
        ministeck = initializer.initConfigurationMinisteck();
    }

    /**
     * Returns a copy of the configuration.
     *
     * @author Adrian Schuetz
     * @param configuration
     * @return configuration (copy)
     */
    public Configuration configurationCopy(final Configuration configuration) {
        final Configuration configCopy = new Configuration(
                configuration.getName(),
                new BasisElement(configuration.getBasisName(),
                        configuration.getBasisWidth(),
                        configuration.getBasisHeight(),
                        configuration.getBasisWidthMM()),
                configuration.getBasisStability(),
                configuration.getBasisCosts(), configuration.getMaterial());
        final Enumeration<ColorObject> colorEnum = configuration.getAllColors();

        while (colorEnum.hasMoreElements()) {
            configCopy.setColor((ColorObject) colorEnum.nextElement());
        }

        final Enumeration<ElementObject> elementEnum = configuration
                .getAllElements();
        // Basis element is already added by the constructor
        elementEnum.nextElement();

        while (elementEnum.hasMoreElements()) {
            configCopy.setElement((ElementObject) elementEnum.nextElement());
        }

        return configCopy;
    }

    /**
     * Returns the configuration (specified by number).
     *
     * @author Adrian Schuetz
     * @param number
     * @return configuration
     */
    public Configuration getSystemConfiguration(final int number) {
        Configuration configuration = new Configuration();
        switch (number) {
        case 0:
            configuration = configurationCopy(legoTop);
            break;
        case 1:
            configuration = configurationCopy(legoSide);
            break;
        case 2:
            configuration = configurationCopy(ministeck);
            break;
        default:
            break;
        }

        return configuration;
    }

    /**
     * Returns configurations.
     *
     * @author Adrian Schuetz
     * @return vector containing configurations
     */
    public Vector<String> getConfiguration() {
        final Vector<String> allConfigurations = new Vector<>();

        for (final Enumeration<String> configurationsEnum = configurations
                .elements(); configurationsEnum.hasMoreElements();) {
            allConfigurations.add(configurationsEnum.nextElement());
        }

        final String[] list = workingDirectory.list();

        for (int i = 0; i < list.length; i++) {
            if (list[i].contains(".cfg")) {
                allConfigurations.add(list[i]);
            }
        }

        return allConfigurations;
    }

    /**
     * Returns mosaic width.
     *
     * @author Adrian Schuetz
     * @return width
     */
    public int getMosaicWidth() {
        return mosaic.getMosaicWidth();
    }

    /**
     * Returns mosaic height.
     *
     * @author Adrian Schuetz
     * @return height
     */
    public int getMosaicHeight() {
        return mosaic.getMosaicHeight();
    }

    /**
     * Calls the appropriate method in mosaic.
     *
     * @author Adrian Schuetz
     * @return Vector[][]
     */
    public List<List<Vector<String>>> getMosaic() {
        return mosaic.getMosaic();
    }

    /**
     * Calls the appropriate method in mosaic.
     *
     * @author Adrian Schuetz
     * @return Vector[][]
     */
    public List<List<Vector<String>>> mosaicCopy() {
        return mosaic.mosaicCopy();
    }

    /**
     * Returns the instance of the mosaic object.
     *
     * @author Adrian Schuetz
     * @return mosaic
     */
    public Mosaic getMosaicInstance() {
        return this.mosaic;
    }

    /**
     * Searches the working directory for folders with same name. If a folder is
     * found a new folder with higher serial-number is generated. starting with
     * serial-number 001.
     *
     * @return Folder name, ending with serial number.
     *
     * @author Adrian Schuetz
     */
    public String generateFolderOutput() {
        String imageName = "";
        int counter = 0;
        // List of file objects from the working directory
        final File[] list = workingDirectory.listFiles();

        // Split image names and ending
        final String[] imageData = originalImage.getImageName().split("\\.");
        imageName = imageData[0];

        // Test if object is a folder
        for (int i = 0; i < list.length; i++) {
            if (list[i].isDirectory()) {
                // Check serial-number
                if (list[i].getName().toLowerCase()
                        .contains(imageName.toLowerCase() + "_")) {
                    try {
                        // Get serial-number (last 3 characters)
                        final String sub = list[i].getName()
                                .substring(list[i].getName().length() - 3);
                        // Get name of folder
                        final String subrest = list[i].getName().substring(0,
                                list[i].getName().length() - 3);

                        // Check imageName
                        if (subrest.toLowerCase()
                                .equals(imageName.toLowerCase() + "_")) {
                            final int number = Integer.parseInt(sub);

                            // Count if folder with higher serial-number is
                            // found
                            if (number > counter) {
                                counter = number;
                            }
                        }
                        // counter = (new Integer(sub).intValue());
                    } catch (final Exception e) {
                        System.out.println(e.toString());
                    }
                }
            }
        }

        // Format output
        final DecimalFormat format = new DecimalFormat("000");
        this.projectName = workingDirectory.getAbsolutePath() + "/" + imageName
                + "_" + format.format(counter + 1);
        this.projectFolder = new File(this.projectName);

        // Create project folder
        this.projectFolder.mkdir();

        // Create subfolder
        final File subFolder = new File(
                projectFolder.getAbsolutePath() + "/data");
        subFolder.mkdir();

        final String folderName = imageName + "_" + format.format(counter + 1);
        return folderName;
    }

    /**
     * Writes the string in a UTF-8 encoded file.
     *
     * @author Adrian Schuetz
     * @param content
     * @param fileName
     * @param subFolder
     * @return true or false
     */
    public boolean generateUTFOutput(final String content,
            final String fileName, final boolean subFolder) {
        String path;
        try {
            if (subFolder) {
                path = projectFolder + "/data/" + fileName;
            } else {
                path = projectFolder + "/" + fileName;
            }
            final OutputStreamWriter output = new OutputStreamWriter(
                    new FileOutputStream(path), "UTF-8");
            output.write(content);
            output.close();
        } catch (final IOException e) {
            System.out.println(e);
            return false;
        }

        return true;
    }

    /**
     * Saves the image in the subfolder data (JPEG compression with maximum
     * quality).
     *
     * @author Adrian Schuetz
     * @param image
     * @param filename
     * @return true or false
     */
    public boolean generateImageOutput(final BufferedImage image,
            final String filename) {
        // The image is written to the file by the writer
        final File file = new File(
                projectFolder + "/data/" + filename + ".jpg");
        // Iterator containing all ImageWriter (JPEG)
        final Iterator<ImageWriter> encoder = ImageIO
                .getImageWritersByFormatName("JPEG");
        final ImageWriter writer = encoder.next();
        // Compression parameter (best quality)
        final ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(1.0f);

        // Try to write the image
        try {
            final ImageOutputStream outputStream = ImageIO
                    .createImageOutputStream(file);
            writer.setOutput(outputStream);
            writer.write(null, new IIOImage(image, null, null), param);
            outputStream.flush();
            writer.dispose();
            outputStream.close();
        } catch (final IOException e) {
            System.out.println(e.toString());
            return false;
        }

        return true;
    }

    /**
     * Saves an image of the color in the subfolder data (JPEG compression with
     * maximum quality).
     *
     * @author Adrian Schuetz
     * @param color
     * @param colorName
     * @return true or false
     */
    public boolean generateColorOutput(final Color color,
            final String colorName) {
        final BufferedImage colorImage = new BufferedImage(1, 1,
                BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = colorImage.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, 1, 1);
        return generateImageOutput(colorImage, colorName);
    }

    /**
     * Set current configuration.
     *
     * @author Adrian Schuetz
     * @param configuration
     */
    public void setCurrentConfiguration(final Configuration configuration) {
        this.current = configuration;
    }

    /**
     * Returns current configuration.
     *
     * @author Adrian Schuetz
     * @return configuration
     */
    public Configuration getCurrentConfiguration() {
        return this.current;
    }

    /**
     * Set current working directory.
     *
     * @author Adrian Schuetz
     * @param file
     */
    public void setWorkingDirectory(final File file) {
        this.workingDirectory = file;
    }

    /**
     * True if an image is selected.
     *
     * @author Adrian Schuetz
     * @return true or false
     */
    public boolean isImage() {
        return !(originalImage == null);
    }

    /**
     * True if a configuration is selected.
     *
     * @author Adrian Schuetz
     * @return true or false
     */
    public boolean isConfiguration() {
        return !(this.current == null);
    }

    /**
     * Returns the current working directory.
     *
     * @author Adrian Schuetz
     * @return working directory
     */
    public File getWorkingDirectory() {
        return this.workingDirectory;
    }

    /**
     * Saves the configuration.
     *
     * @param configuration the configuration to save.
     *
     * @author Adrian Schuetz
     * @exception IOException
     */
    public void configurationSave(final Configuration configuration)
            throws IOException {
        try {
            final FileOutputStream file = new FileOutputStream(
                    workingDirectory + "/" + configuration.getName() + ".cfg");
            final ObjectOutputStream object = new ObjectOutputStream(file);
            object.writeObject(configuration);
            object.close();
        } catch (final IOException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Loads the configuration.
     *
     * @param configuration the name of the configuration to load.
     *
     * @return the cofiguration just loaded.
     *
     * @author Adrian Schuetz
     * @exception IOException
     */
    public Configuration configurationLoad(final String configuration)
            throws IOException {
        Configuration confi = new Configuration();

        try {
            final FileInputStream file = new FileInputStream(
                    workingDirectory + "/" + configuration);
            final ObjectInputStream object = new ObjectInputStream(file);
            confi = (Configuration) object.readObject();
            object.close();
        } catch (final ClassNotFoundException e) {
            System.out.println(e.toString());
        } catch (final IOException e) {
            System.out.println(e.toString());
        }

        return confi;
    }

    /**
     * Resets the image object.
     *
     * @author Adrian Schuetz
     */
    public void imageReset() {
        this.originalImage = null;
    }

    /**
     * Checks if the image file type is valid and creates an image.
     *
     * @author Adrian Schuetz
     * @param file
     * @return true or false
     */
    public boolean imageLoad(final File file) {
        if (file != null) {
            try {
                originalImage = new Picture(file);
                if (originalImage.getImage() == null) {
                    // invalid image file
                    return false;
                }
            } catch (final IOException e) {
                // error loading the image
                System.out.println(e.toString());
                return false;
            }
            return true;
        } else {
            // image loading dialog is canceled
            return false;
        }
    }

    /**
     * Calls the method cutout from originalImage.
     *
     * @author Adrian Schuetz
     * @param cutout
     */
    public void replaceImageByCutout(final Rectangle cutout) {
        originalImage.cutout(cutout);
    }

    /**
     * Calls the method getScaledImage (scaling by sliderValue).
     *
     * @author Adrian Schuetz
     * @param isMosaicImage (true/false)
     * @param sliderValue
     * @return BufferedImage (scaled image)
     */
    public BufferedImage getScaledImage(final boolean isMosaicImage,
            final int sliderValue) {
        if (isMosaicImage) {
            return mosaicImage.getScaledImage(sliderValue);
        } else {
            return originalImage.getScaledImage(sliderValue);
        }
    }

    /**
     * Calls the method getImage.
     *
     * @author Adrian Schuetz
     * @param isMosaicImage (true/false)
     * @return BufferedImage
     */
    public BufferedImage getImage(final boolean isMosaicImage) {
        if (isMosaicImage) {
            return mosaicImage.getImage();
        } else {
            return originalImage.getImage();
        }
    }

    /**
     * Generates a new mosaic.
     *
     * @author Adrian Schuetz
     * @param width
     * @param height
     */
    public void generateMosaic(final int width, final int height) {
        this.mosaic = new Mosaic(width, height, this);
    }

    /**
     * Generates an image from the mosaic.
     *
     * @param threeDInfo <code>true</code> if 3D effect is on.
     *
     * @author Adrian Schuetz
     */
    public void generateMosaicImage(final boolean threeDInfo) {
        this.mosaicImage = null;
        this.mosaic.generateMosaicImage(this.getCurrentConfiguration(),
                threeDInfo);
    }

    /**
     * Sets the variable mosaicImage.
     *
     * @param image the mosaic image.
     *
     * @author Adrian Schuetz
     */
    public void setMosaicImage(final BufferedImage image) {
        this.mosaicImage = new Picture(image);
        dataProcessing.showMosaic();
    }

    /**
     * Changes the value of the display.
     *
     * @author Adrian Schuetz
     * @param value
     * @param number = 1:quantisation, 2:tiling und 3:painting
     */
    public void refreshProgressBarAlgorithm(final int value, final int number) {
        dataProcessing.refreshProgressBarAlgorithm(value, number);
    }

    /**
     * Returns the aspect ratio of the basis element.
     *
     * @author Adrian Schuetz
     * @return aspect ratio basis element
     */
    public Dimension getConfigurationDimension() {
        final Dimension d = new Dimension(current.getBasisWidth(),
                current.getBasisHeight());
        return d;
    }

    /**
     * Calls the method computeScaleFactor.
     *
     * @author Adrian Schuetz
     * @param isMosaicImage (true/false)
     * @param width
     * @param height
     */
    public void computeScaleFactor(final boolean isMosaicImage,
            final double width, final double height) {
        if (isMosaicImage) {
            mosaicImage.computeScaleFactor(width, height);
        } else {
            originalImage.computeScaleFactor(width, height);
        }
    }

    /**
     * Saves information about the current working directory.
     *
     * @author Adrian Schuetz
     * @param currentWorkingDirectory
     * @return true or false
     */
    public boolean saveWorkingDirectory(final File currentWorkingDirectory) {
        try {
            final FileOutputStream file = new FileOutputStream(
                    "workingDirectory.ser");
            final ObjectOutputStream outputstream = new ObjectOutputStream(
                    file);
            outputstream.writeObject(currentWorkingDirectory);
            outputstream.close();
            return true;
        } catch (final IOException e) {
            // Error writing the file
            System.out.println(e.toString());
            return false;
        }
    }

    /**
     * Loads information about the current working directory (if available).
     *
     * @author Adrian Schuetz
     * @return working directory
     */
    public File loadWorkingDirectory() {
        try {
            File currentDirectory;
            final FileInputStream file = new FileInputStream(
                    "workingDirectory.ser");
            final ObjectInputStream inputstream = new ObjectInputStream(file);
            currentDirectory = (File) inputstream.readObject();
            inputstream.close();
            return currentDirectory;
        } catch (final IOException io) {
            System.out.println(io.toString());
            return null;
        } catch (final ClassNotFoundException classNotFound) {
            return null;
        }
    }
}
