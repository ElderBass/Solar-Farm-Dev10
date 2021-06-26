package learn.solarFarm.models;

public enum Material {

    MULTI_SI("Multi-Si"),
    MONO_SI("Mono-Si"),
    AMORPHOUS_SI("A-Si"),
    CDTE("CdTe"),
    CIGS("CIGS");

    private final String abbreviation;

    Material(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return this.abbreviation;
    }


}
