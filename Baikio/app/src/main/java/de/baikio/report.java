package de.baikio;

/**
 * Created by TimoStricker on 22.09.15.
 */
public class report {

    private String _Title;
    private String _Location;
    private String _damageType;

    public report() {};

    public report(String Title, String Location, String damageType) {
        this._Title = Title;
        this._Location = Location;
        this._damageType = damageType;
    }

    public String get_Title(){return _Title;}
    public String get_damageType(){return _damageType;}
    public String get_Location() {return _Location;}
}