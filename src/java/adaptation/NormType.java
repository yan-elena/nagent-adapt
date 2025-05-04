package adaptation;

/**
 * A enumeration to represent the type of norms.
 */
public enum NormType {

    /**
     * Constitutive norm.
     */
    CONSTITUTIVE("constitutive"),
    /**
     * Regimented norm.
     */
    REGIMENTED("regiment"),
    /**
     * Regulative norm.
     */
    REGULATIVE("regulative"),
    /**
     * Sanction rule.
     */
    SANCTION("sanction");

    private final String type;

    /**
     * Constructor of NormType.
     * @param type the type of the norm
     */
    NormType(String type) {
        this.type = type;
    }

    /**
     * Retrieves the type of the norm as string.
     * @return type
     */
    public String getType() {
        return this.type;
    }
}
