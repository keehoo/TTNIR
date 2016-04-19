package com.multivonex.keehoo.thetotallynewinsurancereminder;

/**
 * Created by keehoo on 19.04.2016.
 */
public class Car {

    public String numer_Rejestracyjny;
    public long data_Ubezpieczenia;
    public long dataPrzegladuTechnicznego;
    public String markaSamochodu;

    public String getNumer_Rejestracyjny() {
        return numer_Rejestracyjny;
    }

    public void setNumer_Rejestracyjny(String numer_Rejestracyjny) {
        this.numer_Rejestracyjny = numer_Rejestracyjny;
    }

    public long getData_Ubezpieczenia() {
        return data_Ubezpieczenia;
    }

    public void setData_Ubezpieczenia(long data_Ubezpieczenia) {
        this.data_Ubezpieczenia = data_Ubezpieczenia;
    }

    public long getDataPrzegladuTechnicznego() {
        return dataPrzegladuTechnicznego;
    }

    public void setDataPrzegladuTechnicznego(long dataPrzegladuTechnicznego) {
        this.dataPrzegladuTechnicznego = dataPrzegladuTechnicznego;
    }

    public String getMarkaSamochodu() {
        return markaSamochodu;
    }

    public void setMarkaSamochodu(String markaSamochodu) {
        this.markaSamochodu = markaSamochodu;
    }

    public Car(String numer_Rejestracyjny, long data_Ubezpieczenia, long dataPrzegladuTechnicznego, String markaSamochodu) {
        this.numer_Rejestracyjny = numer_Rejestracyjny;
        this.data_Ubezpieczenia = data_Ubezpieczenia;
        this.dataPrzegladuTechnicznego = dataPrzegladuTechnicznego;
        this.markaSamochodu = markaSamochodu;
    }
}

