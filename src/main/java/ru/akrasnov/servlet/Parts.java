package ru.akrasnov.servlet;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aleksander on 18.12.16.
 */

@Entity
@Table(name = "test_table", schema = "public")
public class Parts implements Serializable {

    @Column(name = "pn")
    private String pn;

    @Column(name = "part_name")
    private String partName;

    @Column(name = "vendor")
    private String vendor;

    @Column(name = "qty")
    private Integer qty;

    @Column(name = "shipped")
    private Date shipped;

    @Column(name = "received")
    private Date received;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id")
    private Integer id;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setPn(String pn) {
        this.pn = pn;
    }

    public String getPn() {
        return pn;
    }

    public void setPartName (String partName) {
        this.partName = partName;
    }

    public String getPartName () {
        return partName;
    }

    public void setQty(Integer oty) {
        this.qty = oty;
    }

    public Integer getQty() {
        return qty;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getVendor() {
        return vendor;
    }

    public  void setShipped(Date shipped) {
        this.shipped = shipped;
    }

    public Date getShipped() {
        return shipped;
    }

    public void setReceived(Date received) {
        this.received = received;
    }

    public Date getReceived(){
        return received;
    }

    private String convertDate(Date date) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("01", "Jan");
        map.put("02", "Feb");
        map.put("03", "Mar");
        map.put("04", "Apr");
        map.put("05", "May");
        map.put("06", "Jun");
        map.put("07", "Jul");
        map.put("08", "Aug");
        map.put("09", "Sep");
        map.put("10", "Oct");
        map.put("11", "Nov");
        map.put("12", "Dec");

        String stringRepresentation = date.toString();
        List<String> dateList = Arrays.asList(stringRepresentation.split("-"));
        String year = dateList.get(0);
        String month = dateList.get(1);
        String day = dateList.get(2);
        return map.get(month) + " " + day + "," + " " + year;
    }

    public String getPartInHtml() {
        return
                "<td>" + pn + "</td>" +
                "<td>" + partName + "</td>" +
                "<td>" + vendor + "</td>" +
                "<td>" + qty + "</td>" +
                "<td>" + convertDate(shipped) + "</td>" +
                "<td>" + convertDate(received) + "</td>";

    }
}
