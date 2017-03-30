package com.restaurant.rakshith;

/**
 * TABLE CLASS.
 * ┬────────────────────────────────────────────────────┬
 * <p>
 * Created by Rakshith on 3/17/2017.
 * This class is responsible for tables in the restaurant. Supports
 * cloning and comparing. This class cannot be extended. The static index
 * field is important to keep a track of the available insertion of tables.
 * It is important to initialize it at the beginning of the field declaration
 * and not in the constructor. The Servers object describes the particular server
 * who serves this table. Table size must be valid in terms of the values
 * passed. The party object here describes the party seated at this particular
 * table. Initially, when there is no party, it is set to null. The other fields
 * except servers are primitive and therefore this entire class
 * cloned would perform a deep copy.
 */
public final class Table implements Cloneable, Comparable<Table> {

    private int id;
    private boolean occupiedStatus;
    private Servers server;
    public static int index = 0;
    private Party party;
    private int capacity;

    public Table(int id, int capacity) {
        if (id > 0)
            this.id = id;
        else
            throw new IllegalArgumentException
                    ("Enter Table id larger than or equal to 0");

        if (capacity > 0) {
            this.capacity = capacity;
            occupiedStatus = false;
            server = null;
            party = null;
        } else
            throw new IllegalArgumentException
                    ("Enter table size larger than or equal to 0");

    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public boolean getOccupiedStatus() {
        return occupiedStatus;
    }

    public void setOccupiedStatus(boolean occupiedStatus) {
        this.occupiedStatus = occupiedStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Servers getServer() {
        return server;
    }

    public void setServer(Servers servers) {
        this.server = servers;
    }

    public void readjustTable() {
        party = null;
        server = null;
        occupiedStatus = false;
    }

    @Override
    public int hashCode() {
        int a = 743;
        a = 1371 * server.hashCode() + id * 17 * a;
        a = ((occupiedStatus) ? 1231 : 1237) * a;
        a = index * a + party.hashCode() * capacity;
        return a;
    }


    /**
     * Matches Equality for state based comparision.
     * The tables are equal only if they have the same
     * capacity, occupiedStatus & have the same server.
     *
     * @param obj other object to be compared
     * @return true when the tables have the same capacity,
     * occupiedStatus & the same server serves them.
     */

    @Override
    public boolean equals(Object obj) {
        if (obj != null && getClass() == obj.getClass()) {
            Table objTable = (Table) obj;
            return capacity == objTable.capacity &&
                    occupiedStatus == objTable.occupiedStatus &&
                    server.equals(objTable.server);
        }
        return false;
    }

    /**
     * Performs a deep copy of the table object.
     * copies the server and party objects into the
     * cloned object additionally with the primitives.
     *
     * @return cloned Table object.
     */
    @Override
    public Table clone() {
        try {
            Table copy = (Table) super.clone();
            copy.server = this.server.clone();
            copy.party = this.party.clone();
            return copy;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Table id: " + id + " occupied status: " +
                (occupiedStatus ? " occupied by(" + party.getName() + " , " +
                        party.getSize() + ")" : " Not Occupied ") +
                " Capacity :" + capacity
                + " Served by : "
                + (server == null ? "None" : "Server #" + server.getId())
                + "\n";
    }

    @Override
    public int compareTo(Table o) {
        return capacity - o.capacity;
    }


}
