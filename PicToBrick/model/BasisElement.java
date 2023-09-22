package pictobrick.model;

/**
 * Name of basis element with its dimension attributes.
 *
 * @param basisName    the name of the basis element.
 * @param basisWidth   the width of the basis element (ratio).
 * @param basisHeight  the height of the basis element (ratio).
 * @param basisWidthMM the width of the basis element in millimeters.
 */
public record BasisElement(String basisName, int basisWidth,
        int basisHeight, double basisWidthMM) {
}
