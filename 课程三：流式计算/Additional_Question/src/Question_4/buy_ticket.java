package Question_4;

public class buy_ticket {
    public String name;
    public String date;
    public String address;
    public String origin;
    public String destination;
    public buy_ticket(){}
    public buy_ticket(String name,String date,String address,String origin,String destination){
        this.name=name;
        this.date=date;
        this.address=address;
        this.origin=origin;
        this.destination=destination;
    }
    @Override
    public String toString() {
        return "buy_ticket{" +
                "name=" + name +
                ", date='" + date + '\'' +
                ", address='" + address + '\'' +
                ", origin='" + origin +'\''+
                ", destination'="+destination+
                '}';
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getAddress() {
        return address;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
