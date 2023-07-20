import javax.xml.bind.annotation.XmlAttribute;

public class ValCurs {
    @XmlAttribute(name = "NumCode")
    private String numCode;
    @XmlAttribute(name = "CharCode")
    private String charCode;
    @XmlAttribute(name = "Nominal")
    private String nominal;
    @XmlAttribute(name = "Name")
    private String name;
    @XmlAttribute(name = "Value")
    private String value;

    public String getCharCode() {
        return charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNumCode() {
        return numCode;
    }

    public void setNumCode(String numCode) {
        this.numCode = numCode;
    }
}