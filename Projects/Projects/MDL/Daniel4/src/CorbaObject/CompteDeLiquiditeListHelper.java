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
// IDL:CorbaObject/CompteDeLiquiditeList:1.0
//
final public class CompteDeLiquiditeListHelper
{
    public static void
    insert(org.omg.CORBA.Any any, CompteDeLiquidite[] val)
    {
        org.omg.CORBA.portable.OutputStream out = any.create_output_stream();
        write(out, val);
        any.read_value(out.create_input_stream(), type());
    }

    public static CompteDeLiquidite[]
    extract(org.omg.CORBA.Any any)
    {
        if(any.type().equivalent(type()))
            return read(any.create_input_stream());
        else
            throw new org.omg.CORBA.BAD_OPERATION();
    }

    private static org.omg.CORBA.TypeCode typeCode_;

    public static org.omg.CORBA.TypeCode
    type()
    {
        if(typeCode_ == null)
        {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            typeCode_ = orb.create_alias_tc(id(), "CompteDeLiquiditeList", orb.create_sequence_tc(0, CompteDeLiquiditeHelper.type()));
        }

        return typeCode_;
    }

    public static String
    id()
    {
        return "IDL:CorbaObject/CompteDeLiquiditeList:1.0";
    }

    public static CompteDeLiquidite[]
    read(org.omg.CORBA.portable.InputStream in)
    {
        CompteDeLiquidite[] _ob_v;
        int len0 = in.read_ulong();
        _ob_v = new CompteDeLiquidite[len0];
        for(int i0 = 0; i0 < len0; i0++)
            _ob_v[i0] = CompteDeLiquiditeHelper.read(in);
        return _ob_v;
    }

    public static void
    write(org.omg.CORBA.portable.OutputStream out, CompteDeLiquidite[] val)
    {
        int len0 = val.length;
        out.write_ulong(len0);
        for(int i0 = 0; i0 < len0; i0++)
            CompteDeLiquiditeHelper.write(out, val[i0]);
    }
}
