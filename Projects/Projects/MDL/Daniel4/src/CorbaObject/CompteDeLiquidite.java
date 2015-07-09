// **********************************************************************
//
// Generated by the ORBacus IDL to Java Translator
//
// Copyright (c) 2005
// IONA Technologies, Inc.
// Waltham, MA, USA
//
// All Rights Reserved
//
// **********************************************************************

// Version: 4.3.2

package CorbaObject;

//
// IDL:CorbaObject/CompteDeLiquidite:1.0
//
/***/

final public class CompteDeLiquidite implements org.omg.CORBA.portable.IDLEntity
{
    private static final String _ob_id = "IDL:CorbaObject/CompteDeLiquidite:1.0";

    public
    CompteDeLiquidite()
    {
    }

    public
    CompteDeLiquidite(String numeroCompte,
                      float soldeCourant,
                      float soldeDisponible)
    {
        this.numeroCompte = numeroCompte;
        this.soldeCourant = soldeCourant;
        this.soldeDisponible = soldeDisponible;
    }

    public String numeroCompte;
    public float soldeCourant;
    public float soldeDisponible;
}