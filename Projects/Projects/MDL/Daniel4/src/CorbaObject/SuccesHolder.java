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
final public class SuccesHolder implements org.omg.CORBA.portable.Streamable
{
    public Succes value;

    public
    SuccesHolder()
    {
    }

    public
    SuccesHolder(Succes initial)
    {
        value = initial;
    }

    public void
    _read(org.omg.CORBA.portable.InputStream in)
    {
        value = SuccesHelper.read(in);
    }

    public void
    _write(org.omg.CORBA.portable.OutputStream out)
    {
        SuccesHelper.write(out, value);
    }

    public org.omg.CORBA.TypeCode
    _type()
    {
        return SuccesHelper.type();
    }
}