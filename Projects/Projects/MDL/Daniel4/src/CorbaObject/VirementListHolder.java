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
// IDL:CorbaObject/VirementList:1.0
//
final public class VirementListHolder implements org.omg.CORBA.portable.Streamable
{
    public Virement[] value;

    public
    VirementListHolder()
    {
    }

    public
    VirementListHolder(Virement[] initial)
    {
        value = initial;
    }

    public void
    _read(org.omg.CORBA.portable.InputStream in)
    {
        value = VirementListHelper.read(in);
    }

    public void
    _write(org.omg.CORBA.portable.OutputStream out)
    {
        VirementListHelper.write(out, value);
    }

    public org.omg.CORBA.TypeCode
    _type()
    {
        return VirementListHelper.type();
    }
}
