package impulsexchangeclient.entities;

import java.io.File;
import java.sql.Blob;

public class ConstructionEntity {

    public ConstructionEntity() {
    }

    public ConstructionEntity(int ordno, int qty, File scheme) {
        this.ordno = ordno;
        this.qty = qty;
        this.scheme = scheme;
    }

    public int getOrdno() {
        return ordno;
    }

    public void setOrdno(int ordno) {
        this.ordno = ordno;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public File getScheme() {
        return scheme;
    }

    public void setScheme(File scheme) {
        this.scheme = scheme;
    }

    private int ordno;
    private int qty;
    private File scheme;
}
