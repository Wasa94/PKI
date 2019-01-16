package com.dv183222m.pki.com.dv183222m.pki.data;

import com.dv183222m.pki.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbContext {

    public static final DbContext INSTANCE = new DbContext();

    protected DbContext() {
        users = new HashMap<>();
        requests = new HashMap<>();
        initUsers();
        initWorkers();
        initRequests();
    }

    private Map<String, User> users;
    private void initUsers() {
        users.put("Vasa", new User("Vasilije", "Dolic", UserType.Client, "Glavna 10", "011/123-456", "Vasa", "vasa123"));
        users.get("Vasa").setImage(R.drawable.android);
    }

    private void initWorkers() {
        Worker worker;

        users.put("Mare", new User("Marko", "Markovic", UserType.Worker, "Cara Dusana 10", "061/123-456", "Mare", "mare123"));
        worker = users.get("Mare").getWorker();
        worker.setExperience(4);
        worker.setRating(4.5f);
        worker.setTypes(new ArrayList<WorkerType>() {{
            add(WorkerType.Electrician);
            add(WorkerType.Carpenter);
        }});
        users.get("Mare").setImage(R.drawable.android);

        users.put("Pera", new User("Pera", "Peric", UserType.Worker, "Cara Dusana 15", "061/123-654", "Pera", "pera123"));
        worker = users.get("Pera").getWorker();
        worker.setExperience(3);
        worker.setRating(4.2f);
        worker.setTypes(new ArrayList<WorkerType>() {{
            add(WorkerType.Electrician);
            add(WorkerType.Plumber);
        }});
        users.get("Pera").setImage(R.drawable.explorer);

        users.put("Laza", new User("Laza", "Lazarevic", UserType.Worker, "Lazina 2", "062/785-456", "Laza", "Laza123"));
        worker = users.get("Laza").getWorker();
        worker.setExperience(7);
        worker.setRating(2.7f);
        worker.setTypes(new ArrayList<WorkerType>() {{
            add(WorkerType.Carpenter);
        }});
        users.get("Laza").setImage(R.drawable.google);

        users.put("Mika", new User("Mika", "Mikic", UserType.Worker, "Kralja Aleksandra 165", "066/128-888", "Mika", "Mika123"));
        worker = users.get("Mika").getWorker();
        worker.setExperience(2);
        worker.setRating(5f);
        worker.setTypes(new ArrayList<WorkerType>() {{
            add(WorkerType.Electrician);
        }});
        users.get("Mika").setImage(R.drawable.peugeot);

        users.put("Zika", new User("Zika", "Zikic", UserType.Worker, "Nikole Tesle 1", "062/111-456", "Zika", "zika123"));
        worker = users.get("Zika").getWorker();
        worker.setExperience(1);
        worker.setRating(2f);
        worker.setTypes(new ArrayList<WorkerType>() {{
            add(WorkerType.Electrician);
            add(WorkerType.Carpenter);
            add(WorkerType.Plumber);
        }});
        users.get("Zika").setImage(R.drawable.bmw);

    }

    private Map<Integer, Request> requests;
    private void initRequests() {
        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        from.set(2018, Calendar.NOVEMBER, 15);
        to.set(2018, Calendar.NOVEMBER, 15);
        Request request = new Request(users.get("Vasa"), users.get("Zika"), "Zemun", "Nikole Tesle 1", from.getTime(), to.getTime(), WorkerType.Plumber, false,5000, "Pukla cev.");
        request.setStatus(RequestStatus.New);
        requests.put(request.getId(), request);

        from.set(2018, Calendar.DECEMBER, 10);
        to.set(2018, Calendar.DECEMBER, 10);
        request = new Request(users.get("Vasa"), users.get("Mare"), "Zemun", "Nikole Tesle 1", from.getTime(), to.getTime(), WorkerType.Electrician, true,2000, "Ne radi bojler.");
        request.setStatus(RequestStatus.Unsuccessful);
        requests.put(request.getId(), request);

        from.set(2018, Calendar.DECEMBER, 28);
        to.set(2018, Calendar.DECEMBER, 30);
        request = new Request(users.get("Vasa"), users.get("Mare"), "Zemun", "Nikole Tesle 1", from.getTime(), to.getTime(), WorkerType.Electrician, false,6000, "Promena instalacija.");
        request.setStatus(RequestStatus.Successful);
        requests.put(request.getId(), request);
    }

    public List<Request> getRequestsClient(String username) {
        List<Request> requestsTmp = new ArrayList<>();

        for(Request request: requests.values()) {
            if(request.getClient() != null && request.getClient().getUsername().equals(username)) {
                requestsTmp.add(request);
            }
        }

        return requestsTmp;
    }

    public List<Request> getRequestsClient(String username, String workerName, List<WorkerType> types, List<RequestStatus> statuses,
                                           int priceMin, int priceMax, Date dateFrom, Date dateTo) {
        List<Request> requestsTmp = new ArrayList<>();

        for(Request request: requests.values()) {
            if(request.getClient() != null && request.getClient().getUsername().equals(username) && request.getWorker().getFullName().contains(workerName)
                    && request.getPrice() >= priceMin && request.getPrice() <= priceMax
                    && request.getFrom().after(dateFrom) && request.getTo().before(dateTo)
                    && statuses.contains(request.getStatus()) && types.contains(WorkerType.getType(request.getType()))) {
                requestsTmp.add(request);
            }
        }

        return requestsTmp;
    }

    public List<Request> getRequestsWorker(String username) {
        List<Request> requestsTmp = new ArrayList<>();

        for(Request request: requests.values()) {
            if(request.getWorker() != null && request.getWorker().getUsername().equals(username)) {
                requestsTmp.add(request);
            }
        }

        return requestsTmp;
    }

    public Request getRequest(int id) {
        return requests.get(id);
    }

    public User getUser(String username) {
        User user = users.get(username);

        return user;
    }

    public boolean addUser(User user) {
        if(user == null)
            return false;

        if(getUser(user.getUsername()) != null)
            return false;

        users.put(user.getUsername(), user);
        return true;
    }

    public List<Worker> getWorkers() {
        List<Worker> workers = new ArrayList<>();

        for(User user: users.values()) {
            if(user.getWorker() != null && user.getWorker().getTypes().size() > 0) {
                workers.add(user.getWorker());
            }
        }

        return workers;
    }

    public List<Worker> getWorkers(String firstName, String lastName, List<WorkerType> type, float minRating, float maxRating, float minExp, float maxExp) {
        List<Worker> workers = new ArrayList<>();

        for(User user: users.values()) {
            if(user.getWorker() != null && user.getWorker().getTypes().size() > 0) {
                if(user.getFirstName().contains(firstName) && user.getLastName().contains(lastName)
                        && user.getWorker().getRating() >= minRating && user.getWorker().getRating() <= maxRating
                        && user.getWorker().getExperience() >= minExp && user.getWorker().getExperience() <= maxExp
                        && user.getWorker().getTypes().containsAll(type))
                workers.add(user.getWorker());
            }
        }

        return workers;
    }

    private Map<Integer, Request> newRequests = new HashMap<>();
    public void createRequest(Request request) {
        newRequests.put(request.getId(), request);
    }

    public void submitRequest(int id) {
        Request request = newRequests.get(id);
        if(request != null) {
            requests.put(id, request);
            newRequests.remove(id);
        };
    }

    public boolean cancelRequest(int id) {
        Request request = requests.get(id);
        if(request != null && request.getStatus() == RequestStatus.New) {
            requests.remove(id);
            return true;
        }
        else {
            return false;
        }
    }
}
