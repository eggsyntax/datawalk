(ns datawalk.example-data)

(def tiny-example {:a 1 :b {:c #{2 3 4} :d "5" :e [6 7 {:f "8" :g {:h :9}}]}})

(def large-example
  '{:ui-root :harmonium.ui.client.pages.bill-entry/BillEntryPage,
 :ui-data nil,
 :harmonium.ui.client.forms.ranges/restrictions
 #:harmonium.ui.client.pages.bill-entry{:bill-entry
                                        #:ti.bill{:carrier
                                                  #{:ti.freight-bill/service-level
                                                    :ti.freight-bill/mode}}},
 :net
 {:request-counters {nil 24},
  :status {nil :ok},
  :response-times {nil "2017-10-22T00:32:23.176Z"},
  :response-counters {nil 24}},
 :harmonium.ui.client.forms.validation/validations
 #:harmonium.ui.client.pages.bill-entry{:bill-entry
                                        {#{[:ti.freight-detail/handling-units]}
                                         [],
                                         #{[:ti.freight-bill/carrier-remit-to]}
                                         [],
                                         #{[:ti.bill/account]} [],
                                         #{[:ti.bill/carrier]} [],
                                         #{[:ti.bill/received]} [],
                                         #{[:ti.bill/direction]} [],
                                         #{[:ti.freight-bill/date]
                                           [:ti.bill/received]}
                                         [],
                                         #{[:ti.bill/consignee]} [],
                                         #{(:ti.freight-bill/bills-of-lading
                                            :values
                                            -3
                                            :ti.bill/freight-details
                                            :values
                                            -4
                                            :ti.freight-detail/nmfc)
                                           (:ti.freight-bill/bills-of-lading
                                            :values
                                            -3
                                            :ti.bill/freight-details
                                            :values
                                            -4
                                            :ti.freight-detail/class)}
                                         [],
                                         #{[:ti.bill/carrier]
                                           [:ti.bill/pro]}
                                         (),
                                         #{(:ti.freight-bill/bills-of-lading
                                            :values
                                            -3
                                            :ti.bill/direction)
                                           [:ti.bill/account]
                                           (:ti.freight-bill/bills-of-lading
                                            :values
                                            -3
                                            :ti.bill/shipper)}
                                         [],
                                         #{[:ti.freight-bill/service-level]}
                                         [],
                                         #{[:ti.bill/shipper]} [],
                                         #{(:ti.freight-bill/bills-of-lading
                                            :values
                                            -3
                                            :ti.bill/direction)
                                           [:ti.bill/account]
                                           (:ti.freight-bill/bills-of-lading
                                            :values
                                            -3
                                            :ti.bill/consignee)}
                                         [],
                                         #{[:ti.freight-detail/weight]}
                                         [],
                                         #{[:ti.bill/terms]} [],
                                         #{[:ti.bill/pro]} [],
                                         #{[:ti.bill/account]
                                           (:ti.freight-bill/bills-of-lading
                                            :values
                                            -3
                                            :ti.bill-of-lading/number)}
                                         [],
                                         #{[:ti.freight-bill/mode]} [],
                                         #{(:ti.freight-bill/bills-of-lading
                                            :values
                                            -3
                                            :ti.bill/freight-details
                                            :values
                                            -4
                                            :ti.freight-detail/handling-units)}
                                         []}},
 :notifications (),
 :current
 #:harmonium.ui.client.pages.bill-entry{:bill-entry
                                        {:ti.freight-bill/date nil,
                                         :ti.bill/received
                                         #inst "2017-10-21T00:00:00.000-00:00",
                                         :ti.freight-bill/mode
                                         {:db/ident
                                          :ti.freight-bill.mode/less-than-truckload,
                                          :ti.enum.val/label "LTL"},
                                         :ti.freight-bill/carrier-remit-to
                                         {:db/id 17592186157318,
                                          :ti.location/address
                                          {:db/id 17592186157319,
                                           :ti.xal.address-detail/administrative-area
                                           {:db/id 17592186157320,
                                            :ti.xal.administrative-area/name
                                            "GA"},
                                           :ti.xal.address-detail/locality
                                           {:db/id 17592186157321,
                                            :ti.xal.locality/name
                                            "ATLANTA"},
                                           :ti.xal.address-detail/thoroughfare
                                           {:db/id 17592186157322,
                                            :ti.xal.thoroughfare/address-lines
                                            {:meta :ref-many-map,
                                             :values
                                             {17592186157323
                                              #:ti.xal.address-line{:index
                                                                    0,
                                                                    :value
                                                                    "PO BOX 198475"}}},
                                            :ti.xal.thoroughfare/postal-code
                                            {:db/id 17592186157324,
                                             :ti.xal.postal-code/number
                                             "30384"}}},
                                          :ti.location/id
                                          #uuid "585ac0cd-fd7f-423b-b72e-abb43390d266"},
                                         :ti.bill/carrier
                                         {:db/id 17592186157316,
                                          :ti.carrier/name
                                          "OLD DOMINION FREIGHT LINE, INC.",
                                          :ti.carrier/scac "ODFL",
                                          :ti.carrier/pro-format
                                          "###########",
                                          :ti.carrier/bill-value-restrictions
                                          {:meta :ref-many-map,
                                           :values
                                           {17592186157326
                                            #:ti.carrier.bill-value-restriction{:attr
                                                                                :ti.freight-bill/mode,
                                                                                :values
                                                                                {:meta
                                                                                 :ref-many-map,
                                                                                 :values
                                                                                 {17592186045808
                                                                                  #:db{:ident
                                                                                       :ti.freight-bill.mode/less-than-truckload},
                                                                                  17592186045809
                                                                                  #:db{:ident
                                                                                       :ti.freight-bill.mode/truckload},
                                                                                  17592186045813
                                                                                  #:db{:ident
                                                                                       :ti.freight-bill.mode/ocean},
                                                                                  17592186045814
                                                                                  #:db{:ident
                                                                                       :ti.freight-bill.mode/air}}}},
                                            17592186157327
                                            #:ti.carrier.bill-value-restriction{:attr
                                                                                :ti.freight-bill/service-level,
                                                                                :values
                                                                                {:meta
                                                                                 :ref-many-map,
                                                                                 :values
                                                                                 {17592186045815
                                                                                  #:db{:ident
                                                                                       :ti.freight-bill.service-level/expedited},
                                                                                  17592186045816
                                                                                  #:db{:ident
                                                                                       :ti.freight-bill.service-level/guaranteed},
                                                                                  17592186045819
                                                                                  #:db{:ident
                                                                                       :ti.freight-bill.service-level/standard}}}}}}},
                                         :ti.bill/pro "11111111111",
                                         :ti.bill/account
                                         {:db/id 17592186049213,
                                          :ti.account/name
                                          "FORWARD FOODS, LLC",
                                          :ti.account/number "FORW01"},
                                         :ti.bill/statement-number nil,
                                         :ti.freight-bill/bills-of-lading
                                         {:values
                                          {-3
                                           {:ti.bill/direction
                                            {:db/ident
                                             :ti.bill.direction/third-party,
                                             :ti.enum.val/label "T"},
                                            :ti.bill/terms
                                            {:db/ident
                                             :ti.bill.terms/prepaid,
                                             :ti.enum.val/label
                                             "PREPAID"},
                                            :ti.bill/shipper
                                            {:db/id 17592186076971,
                                             :ti.location/address
                                             {:db/id 17592186076972,
                                              :ti.xal.address-detail/administrative-area
                                              {:db/id 17592186076974,
                                               :ti.xal.administrative-area/name
                                               "NC"},
                                              :ti.xal.address-detail/country
                                              {:db/id 17592186076973,
                                               :ti.xal.country/three-digit-code
                                               "USA"},
                                              :ti.xal.address-detail/locality
                                              {:db/id 17592186076975,
                                               :ti.xal.locality/name
                                               "ASHEVILLE"},
                                              :ti.xal.address-detail/thoroughfare
                                              {:db/id 17592186076976,
                                               :ti.xal.thoroughfare/address-lines
                                               {:meta :ref-many-map,
                                                :values
                                                {17592186076977
                                                 #:ti.xal.address-line{:index
                                                                       0,
                                                                       :value
                                                                       "178 CLINGMAN AVE"}}},
                                               :ti.xal.thoroughfare/postal-code
                                               {:db/id 17592186076978,
                                                :ti.xal.postal-code/number
                                                "28801"}}},
                                             :ti.location/code
                                             "28801178CLASHEV",
                                             :ti.location/id
                                             #uuid "58c15fda-27d2-4151-8d13-226d0c54eec8",
                                             :ti.location/name
                                             "ASHEVILLE METAL FINISHING",
                                             :ti.location/splc
                                             "418550000"},
                                            :ti.bill/consignee
                                            {:db/id 17592186109135,
                                             :ti.location/address
                                             {:db/id 17592186109136,
                                              :ti.xal.address-detail/administrative-area
                                              {:db/id 17592186109138,
                                               :ti.xal.administrative-area/name
                                               "NC"},
                                              :ti.xal.address-detail/country
                                              {:db/id 17592186109137,
                                               :ti.xal.country/three-digit-code
                                               "USA"},
                                              :ti.xal.address-detail/locality
                                              {:db/id 17592186109139,
                                               :ti.xal.locality/name
                                               "ASHEVILLE"},
                                              :ti.xal.address-detail/thoroughfare
                                              {:db/id 17592186109140,
                                               :ti.xal.thoroughfare/address-lines
                                               {:meta :ref-many-map,
                                                :values
                                                {17592186109141
                                                 #:ti.xal.address-line{:index
                                                                       0,
                                                                       :value
                                                                       "240 SARDIS RD"}}},
                                               :ti.xal.thoroughfare/postal-code
                                               {:db/id 17592186109142,
                                                :ti.xal.postal-code/number
                                                "28806"}}},
                                             :ti.location/code
                                             "28806240SAINDUS",
                                             :ti.location/id
                                             #uuid "58c15fda-e6f3-4f6a-b21e-4a55174f60dd",
                                             :ti.location/name
                                             "INDUSTRIES FOR THE BLIND",
                                             :ti.location/splc
                                             "418550000"},
                                            :ti.bill-of-lading/number
                                            nil,
                                            :ti.bill/freight-details
                                            {:values
                                             {-4
                                              #:ti.freight-detail{:nmfc
                                                                  {:db/id
                                                                   17592186224426,
                                                                   :ti.nmfc/class
                                                                   "92",
                                                                   :ti.nmfc/code
                                                                   "10002001",
                                                                   :ti.nmfc/description
                                                                   "HORNS, METAL"},
                                                                  :description
                                                                  nil,
                                                                  :class
                                                                  nil,
                                                                  :handling-units
                                                                  "1",
                                                                  :weight
                                                                  "17"}}}}}},
                                         :ti.freight-bill/service-level
                                         {:db/ident
                                          :ti.freight-bill.service-level/standard,
                                          :ti.enum.val/label
                                          "STANDARD"}}},
 :user-permissions
 {:read
  #{:import-auditor :master-acc :master-states :cust-acct
    :master-carrier
    :ext-bill-cut-ovrd
    :master-funds
    :master-carr-acc
    :master-acc-prof
    :carrier-fuel
    :web-fuel
    :pricing-bench-mrk
    :cust-appl-ctrl
    :master-nmfc
    :prc-bench-mrk-old
    :missing-docs
    :chgnonzeroaudit
    :apcheck-update
    :pricing-bmtl
    :pricing
    :mastergeolocs
    :routing},
  :modify
  #{:mstp-clr-max-cwt :cust-gsrebate :manual-bills :cust-web-setup
    :carr-connect-inq
    :mstp-chg-non-zero
    :inv-receivables
    :mstp-chg-rebate
    :cust-vendor-comp
    :process-bills
    :master-customer
    :mstp-view-gscalc
    :oqueues-admin
    :mstp-clr-max-amt
    :master-ship-loc
    :debug
    :master-add-ons
    :audit-bills
    :oqueues-access
    :multi-stop-audit
    :rate-ware
    :multi-stop-manual
    :clr-max-cwt-audit
    :reconcile-bills
    :mastercuprofile
    :clr-max-amt-audit
    :cust-gainshare
    :customer-inv
    :invoicing
    :cust-sales
    :increase-gsamt
    :chg-tmwbill-stat
    :view-gscalc
    :mstp-increase-gs
    :oqueues-user}},
 :widgets
 #:harmonium.ui.client.pages.bill-entry{:bill-entry
                                        {:ti.freight-bill/date
                                         #:harmonium.ui.client.forms.validation{:fail-infos
                                                                                []},
                                         :ti.bill/received
                                         #:harmonium.ui.client.forms.validation{:fail-infos
                                                                                []},
                                         :ti.freight-bill/mode
                                         {:harmonium.ui.client.forms.ranges/restrictions
                                          #{:ti.freight-bill.mode/air
                                            :ti.freight-bill.mode/ocean
                                            :ti.freight-bill.mode/truckload
                                            :ti.freight-bill.mode/less-than-truckload},
                                          :harmonium.ui.client.forms.validation/fail-infos
                                          [],
                                          :open? false,
                                          :focused-index 0,
                                          :value nil},
                                         :ti.bill/terms
                                         #:harmonium.ui.client.forms.validation{:fail-infos
                                                                                []},
                                         :ti.freight-bill/carrier-remit-to
                                         {:focused-index 0,
                                          :value nil,
                                          :open? false,
                                          :options
                                          ({:db/id 17592186157318,
                                            :ti.location/address
                                            {:db/id 17592186157319,
                                             :ti.xal.address-detail/administrative-area
                                             {:db/id 17592186157320,
                                              :ti.xal.administrative-area/name
                                              "GA"},
                                             :ti.xal.address-detail/locality
                                             {:db/id 17592186157321,
                                              :ti.xal.locality/name
                                              "ATLANTA"},
                                             :ti.xal.address-detail/thoroughfare
                                             {:db/id 17592186157322,
                                              :ti.xal.thoroughfare/address-lines
                                              {:meta :ref-many-map,
                                               :values
                                               {17592186157323
                                                #:ti.xal.address-line{:index
                                                                      0,
                                                                      :value
                                                                      "PO BOX 198475"}}},
                                              :ti.xal.thoroughfare/postal-code
                                              {:db/id 17592186157324,
                                               :ti.xal.postal-code/number
                                               "30384"}}},
                                            :ti.location/id
                                            #uuid "585ac0cd-fd7f-423b-b72e-abb43390d266"}),
                                          :metadata nil,
                                          :harmonium.ui.client.forms.validation/fail-infos
                                          []},
                                         :ti.bill/carrier
                                         {:focused-index 0,
                                          :value nil,
                                          :open? false,
                                          :options
                                          ({:db/id 17592186157316,
                                            :ti.carrier/name
                                            "OLD DOMINION FREIGHT LINE, INC.",
                                            :ti.carrier/scac "ODFL",
                                            :ti.carrier/pro-format
                                            "###########",
                                            :ti.carrier/bill-value-restrictions
                                            {:meta :ref-many-map,
                                             :values
                                             {17592186157326
                                              #:ti.carrier.bill-value-restriction{:attr
                                                                                  :ti.freight-bill/mode,
                                                                                  :values
                                                                                  {:meta
                                                                                   :ref-many-map,
                                                                                   :values
                                                                                   {17592186045808
                                                                                    #:db{:ident
                                                                                         :ti.freight-bill.mode/less-than-truckload},
                                                                                    17592186045809
                                                                                    #:db{:ident
                                                                                         :ti.freight-bill.mode/truckload},
                                                                                    17592186045813
                                                                                    #:db{:ident
                                                                                         :ti.freight-bill.mode/ocean},
                                                                                    17592186045814
                                                                                    #:db{:ident
                                                                                         :ti.freight-bill.mode/air}}}},
                                              17592186157327
                                              #:ti.carrier.bill-value-restriction{:attr
                                                                                  :ti.freight-bill/service-level,
                                                                                  :values
                                                                                  {:meta
                                                                                   :ref-many-map,
                                                                                   :values
                                                                                   {17592186045815
                                                                                    #:db{:ident
                                                                                         :ti.freight-bill.service-level/expedited},
                                                                                    17592186045816
                                                                                    #:db{:ident
                                                                                         :ti.freight-bill.service-level/guaranteed},
                                                                                    17592186045819
                                                                                    #:db{:ident
                                                                                         :ti.freight-bill.service-level/standard}}}}}}}),
                                          :metadata nil,
                                          :harmonium.ui.client.forms.validation/fail-infos
                                          []},
                                         :ti.bill/consignee
                                         #:harmonium.ui.client.forms.validation{:fail-infos
                                                                                []},
                                         :ti.freight-detail/weight
                                         #:harmonium.ui.client.forms.validation{:fail-infos
                                                                                []},
                                         :ti.bill/pro
                                         {:value "11111111111",
                                          :harmonium.ui.client.forms.validation/fail-infos
                                          [],
                                          :previously-invalid? false,
                                          :verifying? false,
                                          :confirm "11111111111",
                                          :warning? false},
                                         :ti.bill/account
                                         {:focused-index 2,
                                          :value nil,
                                          :open? false,
                                          :options
                                          ({:db/id 17592186047060,
                                            :ti.account/name
                                            "AXIUM FOODS",
                                            :ti.account/number
                                            "AXIU01"}
                                           {:db/id 17592186047270,
                                            :ti.account/name
                                            "FEHR FOODS, INC.",
                                            :ti.account/number
                                            "FEHR01"}
                                           {:db/id 17592186049213,
                                            :ti.account/name
                                            "FORWARD FOODS, LLC",
                                            :ti.account/number
                                            "FORW01"}
                                           {:db/id 17592186046650,
                                            :ti.account/name
                                            "IMPLUS FOOTCARE, LLC.",
                                            :ti.account/number
                                            "IMPL01"}
                                           {:db/id 17592186047443,
                                            :ti.account/name
                                            "INTERNATIONAL FOODSOURCE, LLC",
                                            :ti.account/number
                                            "INTE03"}
                                           {:db/id 17592186047448,
                                            :ti.account/name
                                            "MARIO CAMACHO FOODS, LLC",
                                            :ti.account/number
                                            "MARI02"}
                                           {:db/id 17592186047105,
                                            :ti.account/name
                                            "MICHAEL ANTONIO FOOTWEAR GROUP",
                                            :ti.account/number
                                            "MICH01"}
                                           {:db/id 17592186047221,
                                            :ti.account/name
                                            "RUDOLPH FOODS COMPANY, INC.",
                                            :ti.account/number
                                            "RUDO01"}
                                           {:db/id 17592186047250,
                                            :ti.account/name
                                            "RUDOLPH FOODS COMPANY, INC. - BEN7",
                                            :ti.account/number
                                            "RUDO02"}
                                           {:db/id 17592186047882,
                                            :ti.account/name
                                            "STEUBEN FOODS, INC.",
                                            :ti.account/number
                                            "STEU01"}),
                                          :metadata nil,
                                          :harmonium.ui.client.forms.validation/fail-infos
                                          []},
                                         :ti.freight-bill/flags
                                         {:open? false},
                                         :notes
                                         {:notes
                                          #:harmonium.ui.client.partials.aside{:customer
                                                                               nil,
                                                                               :carrier
                                                                               nil}},
                                         :ti.freight-detail/handling-units
                                         #:harmonium.ui.client.forms.validation{:fail-infos
                                                                                []},
                                         :ti.freight-bill/bills-of-lading
                                         {:values
                                          {-3
                                           {:ti.bill/direction
                                            {:open? false,
                                             :focused-index 0,
                                             :value nil,
                                             :harmonium.ui.client.forms.validation/fail-infos
                                             []},
                                            :ti.bill/terms
                                            {:focused-index 0,
                                             :value nil,
                                             :open? false},
                                            :ti.bill/shipper
                                            {:focused-index 0,
                                             :value nil,
                                             :open? false,
                                             :harmonium.ui.client.forms.validation/fail-infos
                                             [],
                                             :options
                                             ({:db/id 17592186076971,
                                               :ti.location/address
                                               {:db/id 17592186076972,
                                                :ti.xal.address-detail/administrative-area
                                                {:db/id 17592186076974,
                                                 :ti.xal.administrative-area/name
                                                 "NC"},
                                                :ti.xal.address-detail/country
                                                {:db/id 17592186076973,
                                                 :ti.xal.country/three-digit-code
                                                 "USA"},
                                                :ti.xal.address-detail/locality
                                                {:db/id 17592186076975,
                                                 :ti.xal.locality/name
                                                 "ASHEVILLE"},
                                                :ti.xal.address-detail/thoroughfare
                                                {:db/id 17592186076976,
                                                 :ti.xal.thoroughfare/address-lines
                                                 {:meta :ref-many-map,
                                                  :values
                                                  {17592186076977
                                                   #:ti.xal.address-line{:index
                                                                         0,
                                                                         :value
                                                                         "178 CLINGMAN AVE"}}},
                                                 :ti.xal.thoroughfare/postal-code
                                                 {:db/id
                                                  17592186076978,
                                                  :ti.xal.postal-code/number
                                                  "28801"}}},
                                               :ti.location/code
                                               "28801178CLASHEV",
                                               :ti.location/id
                                               #uuid "58c15fda-27d2-4151-8d13-226d0c54eec8",
                                               :ti.location/name
                                               "ASHEVILLE METAL FINISHING",
                                               :ti.location/splc
                                               "418550000"}),
                                             :metadata nil},
                                            :ti.bill/consignee
                                            {:focused-index 1,
                                             :value nil,
                                             :open? false,
                                             :harmonium.ui.client.forms.validation/fail-infos
                                             [],
                                             :options
                                             ({:db/id 17592186109103,
                                               :ti.location/address
                                               {:db/id 17592186109104,
                                                :ti.xal.address-detail/administrative-area
                                                {:db/id 17592186109106,
                                                 :ti.xal.administrative-area/name
                                                 "NC"},
                                                :ti.xal.address-detail/country
                                                {:db/id 17592186109105,
                                                 :ti.xal.country/three-digit-code
                                                 "USA"},
                                                :ti.xal.address-detail/locality
                                                {:db/id 17592186109107,
                                                 :ti.xal.locality/name
                                                 "ASHEVILLE"},
                                                :ti.xal.address-detail/thoroughfare
                                                {:db/id 17592186109108,
                                                 :ti.xal.thoroughfare/address-lines
                                                 {:meta :ref-many-map,
                                                  :values
                                                  {17592186109109
                                                   #:ti.xal.address-line{:index
                                                                         0,
                                                                         :value
                                                                         "175 BINGHAM RD"}}},
                                                 :ti.xal.thoroughfare/postal-code
                                                 {:db/id
                                                  17592186109110,
                                                  :ti.xal.postal-code/number
                                                  "28806"}}},
                                               :ti.location/code
                                               "28806175BIBUNCO",
                                               :ti.location/id
                                               #uuid "58c15fda-576f-486c-9878-95edf307adc6",
                                               :ti.location/name
                                               "BUNCOMBE COUNTY BOE-CW",
                                               :ti.location/splc
                                               "418550000"}
                                              {:db/id 17592186109135,
                                               :ti.location/address
                                               {:db/id 17592186109136,
                                                :ti.xal.address-detail/administrative-area
                                                {:db/id 17592186109138,
                                                 :ti.xal.administrative-area/name
                                                 "NC"},
                                                :ti.xal.address-detail/country
                                                {:db/id 17592186109137,
                                                 :ti.xal.country/three-digit-code
                                                 "USA"},
                                                :ti.xal.address-detail/locality
                                                {:db/id 17592186109139,
                                                 :ti.xal.locality/name
                                                 "ASHEVILLE"},
                                                :ti.xal.address-detail/thoroughfare
                                                {:db/id 17592186109140,
                                                 :ti.xal.thoroughfare/address-lines
                                                 {:meta :ref-many-map,
                                                  :values
                                                  {17592186109141
                                                   #:ti.xal.address-line{:index
                                                                         0,
                                                                         :value
                                                                         "240 SARDIS RD"}}},
                                                 :ti.xal.thoroughfare/postal-code
                                                 {:db/id
                                                  17592186109142,
                                                  :ti.xal.postal-code/number
                                                  "28806"}}},
                                               :ti.location/code
                                               "28806240SAINDUS",
                                               :ti.location/id
                                               #uuid "58c15fda-e6f3-4f6a-b21e-4a55174f60dd",
                                               :ti.location/name
                                               "INDUSTRIES FOR THE BLIND",
                                               :ti.location/splc
                                               "418550000"}),
                                             :metadata nil},
                                            :ti.bill-of-lading/number
                                            #:harmonium.ui.client.forms.validation{:fail-infos
                                                                                   []},
                                            :ti.bill/freight-details
                                            {:values
                                             {-4
                                              #:ti.freight-detail{:nmfc
                                                                  {:open?
                                                                   false,
                                                                   :focused-index
                                                                   0,
                                                                   :value
                                                                   nil,
                                                                   :options
                                                                   ({:db/id
                                                                     17592186224426,
                                                                     :ti.nmfc/class
                                                                     "92",
                                                                     :ti.nmfc/code
                                                                     "10002001",
                                                                     :ti.nmfc/description
                                                                     "HORNS, METAL"}
                                                                    {:db/id
                                                                     17592186224425,
                                                                     :ti.nmfc/class
                                                                     "92",
                                                                     :ti.nmfc/code
                                                                     "10002002",
                                                                     :ti.nmfc/description
                                                                     "HORNS, WOODEN, SU, NOT NESTED"}
                                                                    {:db/id
                                                                     17592186224423,
                                                                     :ti.nmfc/class
                                                                     "92",
                                                                     :ti.nmfc/code
                                                                     "10002003",
                                                                     :ti.nmfc/description
                                                                     "HORNS, WOODEN, KD"}
                                                                    {:db/id
                                                                     17592186224428,
                                                                     :ti.nmfc/class
                                                                     "92",
                                                                     :ti.nmfc/code
                                                                     "10002005",
                                                                     :ti.nmfc/description
                                                                     "HORNS, NOI, NOT NESTED"}
                                                                    {:db/id
                                                                     17592186224424,
                                                                     :ti.nmfc/class
                                                                     "92",
                                                                     :ti.nmfc/code
                                                                     "10002006",
                                                                     :ti.nmfc/description
                                                                     "HORNS, NOI, NESTED"}),
                                                                   :metadata
                                                                   nil,
                                                                   :harmonium.ui.client.forms.validation/fail-infos
                                                                   []},
                                                                  :class
                                                                  #:harmonium.ui.client.forms.validation{:fail-infos
                                                                                                         []},
                                                                  :handling-units
                                                                  #:harmonium.ui.client.forms.validation{:fail-infos
                                                                                                         []}}}}}}},
                                         :ti.bill/shipper
                                         #:harmonium.ui.client.forms.validation{:fail-infos
                                                                                []},
                                         :ti.freight-bill/service-level
                                         {:harmonium.ui.client.forms.ranges/restrictions
                                          #{:ti.freight-bill.service-level/standard
                                            :ti.freight-bill.service-level/guaranteed
                                            :ti.freight-bill.service-level/expedited},
                                          :harmonium.ui.client.forms.validation/fail-infos
                                          [],
                                          :focused-index 0,
                                          :value nil,
                                          :open? false},
                                         :ti.bill/direction
                                         #:harmonium.ui.client.forms.validation{:fail-infos
                                                                                []}}}})
