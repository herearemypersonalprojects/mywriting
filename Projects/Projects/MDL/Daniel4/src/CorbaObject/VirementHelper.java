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
// IDL:CorbaObject/Virement:1.0
//
final public class VirementHelper
{
    public static void
    insert(org.omg.CORBA.Any any, Virement val)
    {
        org.omg.CORBA.portable.OutputStream out = any.create_output_stream();
        write(out, val);
        any.read_value(out.create_input_stream(), type());
    }

    public static Virement
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
            org.omg.CORBA.StructMember[] members = new org.omg.CORBA.StructMember[4];

            members[0] = new org.omg.CORBA.StructMember();
            members[0].name = "date";
            members[0].type = DateStructHelper.type();

            members[1] = new org.omg.CORBA.StructMember();
            members[1].name = "numeroDebiteur";
            members[1].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);

            members[2] = new org.omg.CORBA.StructMember();
            members[2].name = "numeroCrediteur";
            members[2].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);

            members[3] = new org.omg.CORBA.StructMember();
            members[3].name = "montant";
            members[3].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_float);

            typeCode_ = orb.create_struct_tc(id(), "Virement", members);
        }

        return typeCode_;
    }

    public static String
    id()
    {
        return "IDL:CorbaObject/Virement:1.0";
    }

    public static Virement
    read(org.omg.CORBA.portable.InputStream in)
    {
        Virement _ob_v = new Virement();
        _ob_v.date = DateStructHelper.read(in);
        _ob_v.numeroDebiteur = in.read_string();
        _ob_v.numeroCrediteur = in.read_string();
        _ob_v.montant = in.read_float();
        return _ob_v;
    }

    public static void
    write(org.omg.CORBA.portable.OutputStream out, Virement val)
    {
        DateStructHelper.write(out, val.date);
        out.write_string(val.numeroDebiteur);
        out.write_string(val.numeroCrediteur);
        out.write_float(val.montant);
    }
}
