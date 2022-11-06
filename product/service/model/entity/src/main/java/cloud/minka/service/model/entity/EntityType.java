package cloud.minka.service.model.entity;

public enum EntityType {
    TENANT("T#", "T#", "Tenant"),
    USER("U#", "U#", "User"),
    LOCATION("L#", "L#", "Location"),
    ITEM("I#", "I#", "Item"),
    ITEM_LOCATION("I#", "L#", "ItemLocation"),
    ORDER("O#", "O#", "Order"),
    ORDER_ITEM("O#", "#I", "OrderItem");

    private final String prefixPK;
    private final String prefixSK;
    private final String description;

    EntityType(String prefixPK, String prefixSK, String description) {
        this.prefixPK = prefixPK;
        this.prefixSK = prefixSK;
        this.description = description;
    }

    public String prefixPK() {
        return prefixPK;
    }

    public String prefixSK() {
        return prefixSK;
    }

    public String description() {
        return description;
    }
}
