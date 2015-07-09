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
// IDL:CorbaObject/Evolution:1.0
//
final public class EvolutionHolder implements org.omg.CORBA.portable.Streamable
{
    public Evolution value;

    public
    EvolutionHolder()
    {
    }

    public
    EvolutionHolder(Evolution initial)
    {
        value = initial;
    }

    public void
    _read(org.omg.CORBA.portable.InputStream in)
    {
        value = EvolutionHelper.read(in);
    }

    public void
    _write(org.omg.CORBA.portable.OutputStream out)
    {
        EvolutionHelper.write(out, value);
    }

    public org.omg.CORBA.TypeCode
    _type()
    {
        return EvolutionHelper.type();
    }
}
