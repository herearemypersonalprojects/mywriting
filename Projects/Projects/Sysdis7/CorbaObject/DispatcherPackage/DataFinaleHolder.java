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

package CorbaObject.DispatcherPackage;

//
// IDL:CorbaObject/Dispatcher/DataFinale:1.0
//
final public class DataFinaleHolder implements org.omg.CORBA.portable.Streamable
{
    public DataFinale value;

    public
    DataFinaleHolder()
    {
    }

    public
    DataFinaleHolder(DataFinale initial)
    {
        value = initial;
    }

    public void
    _read(org.omg.CORBA.portable.InputStream in)
    {
        value = DataFinaleHelper.read(in);
    }

    public void
    _write(org.omg.CORBA.portable.OutputStream out)
    {
        DataFinaleHelper.write(out, value);
    }

    public org.omg.CORBA.TypeCode
    _type()
    {
        return DataFinaleHelper.type();
    }
}