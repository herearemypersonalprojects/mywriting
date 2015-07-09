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
// IDL:CorbaObject/Societe:1.0
//
final public class SocieteHolder implements org.omg.CORBA.portable.Streamable
{
    public Societe value;

    public
    SocieteHolder()
    {
    }

    public
    SocieteHolder(Societe initial)
    {
        value = initial;
    }

    public void
    _read(org.omg.CORBA.portable.InputStream in)
    {
        value = SocieteHelper.read(in);
    }

    public void
    _write(org.omg.CORBA.portable.OutputStream out)
    {
        SocieteHelper.write(out, value);
    }

    public org.omg.CORBA.TypeCode
    _type()
    {
        return SocieteHelper.type();
    }
}
