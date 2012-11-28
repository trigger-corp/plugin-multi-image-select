//
//  ForgeApp.h
//  ForgeCore
//
//  Created by Connor Dunn on 03/10/2012.
//  Copyright (c) 2012 Trigger Corp. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "JSONKit.h"
#import "ForgeAppDelegate.h"

@interface ForgeApp : NSObject {
	
}

@property UIWebView* webView;
@property NSDictionary* appConfig;
@property ForgeAppDelegate* appDelegate;
@property ForgeViewController* viewController;
@property NSMutableArray* eventListeners;

+ (ForgeApp*)sharedApp;
- (id)nativeEvent:(SEL)selector withArgs:(int) count, ...;

@end
