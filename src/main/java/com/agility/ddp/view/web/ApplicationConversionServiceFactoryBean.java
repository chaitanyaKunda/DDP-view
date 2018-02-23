package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpAedReqDocs;
import com.agility.ddp.data.domain.DdpAedRule;
import com.agility.ddp.data.domain.DdpBranch;
import com.agility.ddp.data.domain.DdpCommunicationSetup;
import com.agility.ddp.data.domain.DdpCompany;
import com.agility.ddp.data.domain.DdpCompressionSetup;
import com.agility.ddp.data.domain.DdpDocnameConv;
import com.agility.ddp.data.domain.DdpDoctype;
import com.agility.ddp.data.domain.DdpGenSystem;
import com.agility.ddp.data.domain.DdpGroup;
import com.agility.ddp.data.domain.DdpNotification;
import com.agility.ddp.data.domain.DdpParty;
import com.agility.ddp.data.domain.DdpRole;
import com.agility.ddp.data.domain.DdpRule;
import com.agility.ddp.data.domain.DdpScheduler;
import com.agility.ddp.data.domain.DdpSourceSystem;
import com.agility.ddp.data.domain.DdpUser;

import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.roo.addon.web.mvc.controller.converter.RooConversionService;

/**
 * A central place to register application converters and formatters. 
 */
@RooConversionService
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

//	@Override
//	protected void installFormatters(FormatterRegistry registry) {
////		super.installFormatters(registry);
////		 registry.addConverterFactory(new StringToEntityConverterFactory());
////		 registry.addConverterFactory(new SomeOtherCustomFactory());
//		 Register application converters and formatters
//	}

	public Converter<DdpGroup, String> getDdpGroupToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.agility.ddp.data.domain.DdpGroup, java.lang.String>() {
            public String convert(DdpGroup ddpGroup) {
                //return new StringBuilder().append(ddpGroup.getGrpName()).append(' ').append(ddpGroup.getGrpDisplayName()).append(' ').append(ddpGroup.getGrpDescription()).append(' ').append(ddpGroup.getGrpStatus()).toString();
            	return new StringBuilder().append(ddpGroup.getGrpDisplayName()).toString();
            }
        };
    }

	public Converter<DdpRole, String> getDdpRoleToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.agility.ddp.data.domain.DdpRole, java.lang.String>() {
            public String convert(DdpRole ddpRole) {
                return new StringBuilder().append(ddpRole.getRolDisplayName()).toString();
            }
        };
    }

	public Converter<DdpUser, String> getDdpUserToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.agility.ddp.data.domain.DdpUser, java.lang.String>() {
            public String convert(DdpUser ddpUser) {
                return new StringBuilder().append(ddpUser.getUsrDisplayName()).toString();
            }
        };
    }

	public Converter<DdpAedReqDocs, String> getDdpAedReqDocsToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.agility.ddp.data.domain.DdpAedReqDocs, java.lang.String>() {
            public String convert(DdpAedReqDocs ddpAedReqDocs) {
                return new StringBuilder().append(ddpAedReqDocs.getArdDocType()).append(' ').append(ddpAedReqDocs.getArdDocRequired()).append(' ').append(ddpAedReqDocs.getArdMergeSeq()).append(' ').append(ddpAedReqDocs.getArdStatus()).toString();
            }
        };
    }

	public Converter<DdpAedRule, String> getDdpAedRuleToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.agility.ddp.data.domain.DdpAedRule, java.lang.String>() {
            public String convert(DdpAedRule ddpAedRule) {
                return new StringBuilder().append(ddpAedRule.getAedIsPartyCheckRequired()).append(' ').append(ddpAedRule.getAedPropFile()).append(' ').append(ddpAedRule.getAedPropTable()).append(' ').append(ddpAedRule.getAedPropInUse()).toString();
            }
        };
    }

	public Converter<DdpNotification, String> getDdpNotificationToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.agility.ddp.data.domain.DdpNotification, java.lang.String>() {
            public String convert(DdpNotification ddpNotification) {
//                return new StringBuilder().append(ddpNotification.getNotSuccessEmailAddress()).append(' ').append(ddpNotification.getNotFailureEmailAddress()).append(' ').append(ddpNotification.getNotStatus()).append(' ').append(ddpNotification.getNotCreatedBy()).toString();
                return new StringBuilder().append(ddpNotification.getNotSuccessEmailAddress()).toString();   
            }
        };
    }

	public Converter<DdpScheduler, String> getDdpSchedulerToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.agility.ddp.data.domain.DdpScheduler, java.lang.String>() {
            public String convert(DdpScheduler ddpScheduler) {
//                return new StringBuilder().append(ddpScheduler.getSchStartDate()).append(' ').append(ddpScheduler.getSchEndDate()).append(' ').append(ddpScheduler.getSchTimeFrequency()).append(' ').append(ddpScheduler.getSchTime()).toString();
                return new StringBuilder().append(ddpScheduler.getSchId()).toString();
            }
        };
    }

	public Converter<DdpCompany, String> getDdpCompanyToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.agility.ddp.data.domain.DdpCompany, java.lang.String>() {
            public String convert(DdpCompany ddpCompany) {
//                return new StringBuilder().append(ddpCompany.getComCompanyName()).append(' ').append(ddpCompany.getComCountryCode()).append(' ').append(ddpCompany.getComCountryName()).append(' ').append(ddpCompany.getComRegion()).toString();
            	  return new StringBuilder().append(ddpCompany.getComCompanyName()).toString();
            }
        };
    }

	public Converter<DdpBranch, String> getDdpBranchToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.agility.ddp.data.domain.DdpBranch, java.lang.String>() {
            public String convert(DdpBranch ddpBranch) {
//                return new StringBuilder().append(ddpBranch.getBrnBranchName()).append(' ').append(ddpBranch.getBrnCompnayCode()).append(' ').append(ddpBranch.getBrnStatus()).toString();
                return new StringBuilder().append(ddpBranch.getBrnBranchName()).toString();
            }
        };
    }

	public Converter<DdpCommunicationSetup, String> getDdpCommunicationSetupToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.agility.ddp.data.domain.DdpCommunicationSetup, java.lang.String>() {
            public String convert(DdpCommunicationSetup ddpCommunicationSetup) {
//                return new StringBuilder().append(ddpCommunicationSetup.getCmsCommunicationProtocol()).append(' ').append(ddpCommunicationSetup.getCmsProtocolSettingsId()).append(' ').append(ddpCommunicationSetup.getCmsStatus()).append(' ').append(ddpCommunicationSetup.getCmsCreatedBy()).toString();
                return new StringBuilder().append(ddpCommunicationSetup.getCmsCommunicationProtocol()).toString();
            }
        };
    }

	public Converter<DdpDoctype, String> getDdpDoctypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.agility.ddp.data.domain.DdpDoctype, java.lang.String>() {
            public String convert(DdpDoctype ddpDoctype) {
//                return new StringBuilder().append(ddpDoctype.getDtyDocTypeName()).append(' ').append(ddpDoctype.getDtySource()).append(' ').append(ddpDoctype.getDtyCompanyCode()).append(' ').append(ddpDoctype.getDtyStatus()).toString();
            	  return new StringBuilder().append(ddpDoctype.getDtyDocTypeName()).toString();
            }
        };
    }

//	public Converter<DdpParty, String> getDdpPartyToStringConverter() {
//        return new org.springframework.core.convert.converter.Converter<com.agility.ddp.data.domain.DdpParty, java.lang.String>() {
//            public String convert(DdpParty ddpParty) {
//                return new StringBuilder().append(ddpParty.getPtyPartyName()).toString();
//            }
//        };
//    }

    public Converter<DdpParty, String> getDdpPartyToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.agility.ddp.data.domain.DdpParty, java.lang.String>() {
            public String convert(DdpParty ddpParty) {
                return new StringBuilder().append(ddpParty.getPtyPartyCode()).append(' ').append(ddpParty.getPtyPartyName()).toString();
            }
        };
    }
	
	public Converter<DdpCompressionSetup, String> getDdpCompressionSetupToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.agility.ddp.data.domain.DdpCompressionSetup, java.lang.String>() {
            public String convert(DdpCompressionSetup ddpCompressionSetup) {
//                return new StringBuilder().append(ddpCompressionSetup.getCtsCompressionLevel()).append(' ').append(ddpCompressionSetup.getCtsCompressionSize()).toString();
                return new StringBuilder().append(ddpCompressionSetup.getCtsCompressionLevel()).toString();
            }
        };
    }

	public Converter<DdpDocnameConv, String> getDdpDocnameConvToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.agility.ddp.data.domain.DdpDocnameConv, java.lang.String>() {
            public String convert(DdpDocnameConv ddpDocnameConv) {
//                return new StringBuilder().append(ddpDocnameConv.getDcvCompanyCode()).append(' ').append(ddpDocnameConv.getDcvBranchCode()).append(' ').append(ddpDocnameConv.getDcvDoctypeCode()).append(' ').append(ddpDocnameConv.getDcvGenNamingConv()).toString();
                return new StringBuilder().append(ddpDocnameConv.getDcvGenNamingConv()).toString();
            }
        };
    }

	public Converter<DdpGenSystem, String> getDdpGenSystemToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.agility.ddp.data.domain.DdpGenSystem, java.lang.String>() {
            public String convert(DdpGenSystem ddpGenSystem) {
//                return new StringBuilder().append(ddpGenSystem.getGenCompanyCode()).append(' ').append(ddpGenSystem.getGenServerIp()).append(' ').append(ddpGenSystem.getGenServerName()).append(' ').append(ddpGenSystem.getGenDomain()).toString();
                return new StringBuilder().append(ddpGenSystem.getGenServerName()).toString(); 
            }
        };
    }

	public Converter<DdpSourceSystem, String> getDdpSourceSystemToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.agility.ddp.data.domain.DdpSourceSystem, java.lang.String>() {
            public String convert(DdpSourceSystem ddpSourceSystem) {
//              return new StringBuilder().append(ddpSourceSystem.getSouCompanyCode()).append(' ').append(ddpSourceSystem.getSouServerIp()).append(' ').append(ddpSourceSystem.getSouServerName()).append(' ').append(ddpSourceSystem.getSouDomain()).toString();
                return new StringBuilder().append(ddpSourceSystem.getSouServerName()).toString();
            }
        };
    }
	
	 public Converter<DdpRule, String> getDdpRuleToStringConverter() {
	        return new org.springframework.core.convert.converter.Converter<com.agility.ddp.data.domain.DdpRule, java.lang.String>() {
	            public String convert(DdpRule ddpRule) {
//	            	return new StringBuilder().append(ddpRule.getRulName()).append(' ').append(ddpRule.getRulDescription()).append(' ').append(ddpRule.getRulStatus()).append(' ').append(ddpRule.getRulCreatedBy()).toString();
	                return new StringBuilder().append(ddpRule.getRulName()).append(' ').toString();
	            }
	        };
    }
}
