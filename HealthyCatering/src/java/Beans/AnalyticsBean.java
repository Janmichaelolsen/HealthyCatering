package Beans;

import DB.Database;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean; 
import javax.inject.Named;
import logikk.Dish;
import logikk.Order;
import logikk.StoredOrders;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartSeries;

@ManagedBean
@RequestScoped
@Named("Analytics")
class AnalyticsBean implements Serializable {

    private Database db = new Database();
    private Date toDate = new Date();
    private Date toDateOriginal = toDate;
    private Date fromDate = new Date(toDate.getYear() - 1, toDate.getMonth(), toDate.getDay());
    private Date fromDateOriginal = fromDate;
    private CartesianChartModel linearModel;
    private CartesianChartModel categoryModel;
    private CartesianChartModel categoryModel2;
    private CartesianChartModel categoryModelSales;
    private ArrayList<Order> orders = new ArrayList();
    private float turnoverNow = 0;
    private float turnoverLastYear = 0;
    private String[] months = {"January", "February", "March", "April", "Mai", "June", "July", "August", "September",
        "October", "November", "Descember"};

    public AnalyticsBean() {
        createLinearModel();
        createCategoryModel();
        createCategoryModel2();
        createCategoryModelSales();
    }

    public ArrayList<Order> calculateTurnover(Date fromDate, Date toDate) {
        java.sql.Date fromDateSql = new java.sql.Date(fromDate.getTime());
        java.sql.Date toDateSql = new java.sql.Date(toDate.getTime());
        String query = "SELECT * FROM orders WHERE dates <='" + toDateSql.toString() + "' and dates>='" + fromDateSql.toString() + "'";
        orders = db.getTurnoverstatistics(query);
        return orders;
    }

    public CartesianChartModel getCategoryModel() {
        return categoryModel;
    }

    public CartesianChartModel getCategoryModelSales() {
        return categoryModelSales;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getFromDateOriginal() {
        return fromDateOriginal;
    }

    public Date getToDateOriginal() {
        return toDateOriginal;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public float getTurnoverLastYear() {
        return turnoverLastYear;
    }

    public float getTurnoverNow() {
        return turnoverNow;
    }

    public CartesianChartModel getCategoryModel2() {
        return categoryModel2;
    }

    public CartesianChartModel getLinearModel() {
        return linearModel;
    }

    public void createCategoryModel2() {
        categoryModel2 = new CartesianChartModel();

        ArrayList<StoredOrders> sOrders = getStoredInfo();
        ArrayList<Dish> dishesDb = db.getDishes();

        ChartSeries moneyGenerated = new ChartSeries();
        moneyGenerated.setLabel("Income Generated");
        int moneyGeneratedForDish = 0;
        for (int i = 0; i < dishesDb.size(); i++) {
            for (int u = 0; u < sOrders.size(); u++) {
                if (sOrders.get(u).getDishId() == dishesDb.get(i).getDishId()) {
                    moneyGeneratedForDish += sOrders.get(u).getTotalPrice();
                }
            }
            moneyGenerated.set(dishesDb.get(i).getDishId(), moneyGeneratedForDish);
            moneyGeneratedForDish = 0;
        }
        categoryModel2.addSeries(moneyGenerated);
    }

    public void createCategoryModelSales() {
        ArrayList<StoredOrders> sOrders = getStoredInfo(this.fromDate);
        if (!sOrders.isEmpty() && sOrders != null) {
            for (int i = 0; i < sOrders.size(); i++) {
                if (sOrders.get(i).getSalesmanUsername() == null) {
                    sOrders.remove(sOrders.get(i));
                    i--;
                }
            }

            int salesmenCounter = db.getNumberOfSalesmen();
            String salesmanUsername = null;

            int[] salesNumbers = new int[salesmenCounter];
            String[] salesmenUsernames = new String[salesmenCounter];

            for (int i = 0; i < salesmenCounter; i++) {
                if (!sOrders.isEmpty()) {
                    salesmanUsername = sOrders.get(0).getSalesmanUsername();
                    salesmenUsernames[i] = salesmanUsername;
                    for (int u = 0; u < sOrders.size(); u++) {
                        if (sOrders.get(u).getSalesmanUsername().equals(salesmanUsername)) {
                            salesNumbers[i] += sOrders.get(u).getTotalPrice();
                            sOrders.remove(sOrders.get(u));
                            u--;
                        }
                    }
                }
            }
            categoryModelSales = new CartesianChartModel();
            ChartSeries salesmenPay = new ChartSeries();

            salesmenPay.setLabel("Commision earned");
            for (int i = 0; i < salesmenCounter; i++) {
                salesmenPay.set(salesmenUsernames[i], salesNumbers[i] * 0.11);
            }
            categoryModelSales.addSeries(salesmenPay);
        }
    }

    public void createCategoryModel() {
        categoryModel = new CartesianChartModel();
        ChartSeries dishesCount = new ChartSeries();

        dishesCount.setLabel("Dishes sold");

        ArrayList<StoredOrders> sOrders = getStoredInfo();
        ArrayList<Dish> dishesDb = db.getDishes();

        int numberOfSales = 0;

        for (int i = 0; i < dishesDb.size(); i++) {
            for (int u = 0; u < sOrders.size(); u++) {
                if (sOrders.get(u).getDishId() == dishesDb.get(i).getDishId()) {
                    numberOfSales += sOrders.get(u).getDishCount();
                }
            }
            dishesCount.set(dishesDb.get(i).getDishId(), numberOfSales);
            numberOfSales = 0;
        }
        categoryModel.addSeries(dishesCount);
    }

    public ArrayList<StoredOrders> getStoredInfo() {
        String query = "SELECT * FROM dishes_stored";
        return db.getStoredOrders(query);
    }

    public ArrayList<StoredOrders> getStoredInfo(Date fromDate) {
        java.sql.Date fromDateSql = new java.sql.Date(fromDate.getTime());
        String query = "SELECT * FROM dishes_stored WHERE dates >='" + fromDateSql.toString() + "'";
        return db.getStoredOrders(query);
    }

    public void update() {
        createLinearModel();
        createCategoryModel();
        createCategoryModel2();
        createCategoryModelSales();
    }

    public void createLinearModel() {
        turnoverNow = 0;
        turnoverLastYear = 0;
        ArrayList<Order> orders = calculateTurnover(this.fromDate, this.toDate);
        linearModel = new CartesianChartModel();
        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Turnover this year");
        int monthCounter = this.fromDate.getMonth();
        int spanSize = (this.fromDate.getMonth() < this.toDate.getMonth())
                ? (this.toDate.getMonth() - this.fromDate.getMonth())
                : ((11 + this.toDate.getMonth()) - this.fromDate.getMonth());
        float turnOver = 0;
        for (int i = 0; i < spanSize; i++) {
            if (spanSize == 1) {
                calculateMonthlyTurnover(this.fromDate.getMonth(), series1, orders, true);
                break;
            } else {
                for (int u = 0; u < orders.size(); u++) {
                    if (orders.get(u).getDate().getMonth() == i) {
                        turnOver += orders.get(u).getTotalprice();
                    }
                }
            }
            series1.set(months[monthCounter], turnOver);
            monthCounter++;
            if (monthCounter == 11) {
                monthCounter = 0;
            }
            turnoverNow += turnOver;
            turnOver = 0;
        }

        LineChartSeries series2 = new LineChartSeries();
        series2.setLabel("Turnover last Year");
        orders = calculateTurnover(new Date(this.fromDate.getYear() - 1), this.fromDate);
        monthCounter = this.fromDate.getMonth();
        for (int i = 0; i < spanSize; i++) {
            if (spanSize == 1) {
                calculateMonthlyTurnover(this.fromDate.getMonth(), series2, orders, false);
                break;
            } else {
                for (int u = 0; u < orders.size(); u++) {
                    if (orders.get(u).getDate().getMonth() == i) {
                        turnOver += orders.get(u).getTotalprice();
                    }
                }
            }
            series2.set(months[monthCounter], turnOver);
            monthCounter++;
            if (monthCounter == 11) {
                monthCounter = 0;
            }
            turnoverLastYear += turnOver;
            turnOver = 0;
        }
        linearModel.addSeries(series1);
        linearModel.addSeries(series2);

    }

    private void calculateMonthlyTurnover(int month, LineChartSeries series, ArrayList<Order> orders, boolean now) {
        Calendar calendar = Calendar.getInstance();
        int turnover = 0;
        if (month == 1) {
            for (int i = 0; i < 28; i++) {
                for (int u = 0; u < orders.size(); u++) {
                    calendar.setTime(orders.get(u).getDate());
                    if (calendar.get(Calendar.DAY_OF_MONTH) == i) {
                        turnover += orders.get(u).getTotalprice();
                    }
                }
                series.set(i + 1 + " ", turnover);
                if (now) {
                    turnoverNow += turnover;
                } else {
                    turnoverLastYear += turnover;
                }
                turnover = 0;
            }
        } else if ((month % 2 == 0 && month <= 6) || (month % 2 != 0 && month > 6)) {
            for (int i = 0; i < 31; i++) {
                for (int u = 0; u < orders.size(); u++) {
                    calendar.setTime(orders.get(u).getDate());
                    if (calendar.get(Calendar.DAY_OF_MONTH) == i) {
                        turnover += orders.get(u).getTotalprice();
                    }
                }
                series.set(i + 1 + " ", turnover);
                if (now) {
                    turnoverNow += turnover;
                } else {
                    turnoverLastYear += turnover;
                }
                turnover = 0;
            }
        } else {
            for (int i = 0; i < 30; i++) {
                for (int u = 0; u < orders.size(); u++) {
                    calendar.setTime(orders.get(u).getDate());
                    if (calendar.get(Calendar.DAY_OF_MONTH) == i) {
                        turnover += orders.get(u).getTotalprice();
                    }
                }
                series.set(i + 1 + " ", turnover);
                if (now) {
                    turnoverNow += turnover;
                } else {
                    turnoverLastYear += turnover;
                }
                turnover = 0;
            }
        }
    }
}
