package entites;

import orm.annotations.Column;
import orm.annotations.Id;
import orm.annotations.Table;
import orm.enums.DataType;

@Table(name = "PRODUCTS_TBL")
public class Product {
    @Column(name = "product_id", dataType = DataType.INT, size = 255)
    @Id
    private int id;

    @Column(name = "product_name", dataType = DataType.VARCHAR, size = 255)
    private String name;

    @Column(name = "product_price", dataType = DataType.INT, size = 255)
    private int price;

    @Column(name = "product_unit_in_stock", dataType = DataType.INT, size = 255)
    private int unitInStock;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getUnitInStock() {
        return unitInStock;
    }

    public void setUnitInStock(int unitInStock) {
        this.unitInStock = unitInStock;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", unitInStock=" + unitInStock +
                '}';
    }
}
