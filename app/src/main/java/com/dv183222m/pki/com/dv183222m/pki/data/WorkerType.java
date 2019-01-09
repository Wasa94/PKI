package com.dv183222m.pki.com.dv183222m.pki.data;

public enum WorkerType {
    Plumber,
    Electrician,
    Carpenter;

    public String getWorkType() {
        switch (this) {
            case Plumber:
                return "Plumbing";
            case Electrician:
                return "Electrical";
            case Carpenter:
                return "Carpentry";
            default:
                return "";
        }
    }

    public static WorkerType getType(String type) {
        switch (type) {
            case "Plumbing":
                return Plumber;
            case "Electrical":
                return Electrician;
            case "Carpentry":
                return Carpenter;
            default:
                return Electrician;
        }
    }
}
