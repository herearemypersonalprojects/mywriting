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
// IDL:CorbaObject/OrdreList:1.0
//
final public class OrdreListHolder implements org.omg.CORBA.portable.Streamable
{
    public Ordre[] value;

    public
    OrdreListHolder()
    {
    }

    public
    OrdreListHolder(Ordre[] initial)
    {
        value = initial;
    }

    public void
    _read(org.omg.CORBA.portable.InputStream in)
    {
        value = OrdreListHelper.read(in);
    }

    public void
    _write(org.omg.CORBA.portable.OutputStream out)
    {
        OrdreListHelper.write(out, value);
    }

    public org.omg.CORBA.TypeCode
    _type()
    {
        return OrdreListHelper.type();
    }
}