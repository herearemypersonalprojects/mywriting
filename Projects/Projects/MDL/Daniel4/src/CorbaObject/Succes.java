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
// IDL:CorbaObject/Succes:1.0
//
/***/

final public class Succes implements org.omg.CORBA.portable.IDLEntity
{
    private static final String _ob_id = "IDL:CorbaObject/Succes:1.0";

    public
    Succes()
    {
    }

    public
    Succes(boolean etat,
           String msg)
    {
        this.etat = etat;
        this.msg = msg;
    }

    public boolean etat;
    public String msg;
}
